package de.unijena.bioinf.ChemistryBase.algorithm;

import de.unijena.bioinf.ChemistryBase.data.DataDocument;
import de.unijena.bioinf.ChemistryBase.data.ParameterHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParameterizedByAnnotation<T> {

    private final Class<T> klass;
    private String[] constructorParameters;
    private Constructor<T> constructor;
    private Property[] setter;
    private Property[] getter;

    public ParameterizedByAnnotation(Class<T> klass) {
        this.klass = klass;
        final List<String> constructorParameters = new ArrayList<String>();
        final Set<Property> properties = new HashSet<Property>();
        // search for fields with @Parameter annotation
        final Set<Property> params = new HashSet<Property>();
        for (Field f : klass.getFields()) {
            if (f.isAnnotationPresent(Parameter.class)) {
                final Parameter p = f.getAnnotation(Parameter.class);
                final Property param = new Property(null, p.value().isEmpty() ? f.getName() : p.value(), f.getName());
                params.add(param);
            }
        }
        // search for getter/setter methods with @Parameter annotation
        for (Method m : klass.getMethods()) {
            if (m.isAnnotationPresent(Parameter.class)) {
                final Parameter p = m.getAnnotation(Parameter.class);
                String fieldName = null;
                if (m.getName().startsWith("get")) {
                    fieldName = m.getName().substring(m.getName().indexOf("get")+3);
                } else if (m.getName().startsWith("is")) {
                    fieldName = m.getName().substring(m.getName().indexOf("is")+2);
                } else {
                    throw new RuntimeException("Parameterized methods should start with get<FieldName> or is<FieldName>.");
                }
                fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
                properties.add(new Property(m, p.value().isEmpty() ? fieldName : p.value(), fieldName));
            }
        }
        // search for constructor with @Parameter annotation
        for (Constructor<?> constructor : klass.getConstructors()) {
            final Annotation[][] paramAnnotations = constructor.getParameterAnnotations();
            int k=0;
            for (Annotation[] an : paramAnnotations) {
                for (Annotation a : an) {
                    if (a instanceof Parameter) {
                        this.constructor = (Constructor<T>)constructor;
                        final String name = ((Parameter)a).value();
                        constructorParameters.add(((Parameter) a).value());
                        if (((Parameter) a).value().isEmpty()) throw new RuntimeException("Constructor parameter have to be named");
                        final Property property = new Property(null, name, name);
                        if (!properties.contains(property)) properties.add(property);
                    }
                }
                if (this.constructor != null && constructorParameters.size() < an.length) throw new RuntimeException("All constructor parameters have to contain a @Parameter annotation");
                ++k;
            }
            if (this.constructor!=null) break;
        }
        // now for each parameter:
        // search for getter methods
        for (Property p : properties) {
            if (p.method==null) {
                try {
                    final Method getter = klass.getMethod("get" + Character.toUpperCase(p.fieldName.charAt(0)) + p.fieldName.substring(1));
                    p.method = getter;
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Can't find getter method for parameter '" + p.name + "'");
                }
            }
        }
        // now for each parameter:
        // search for setter methods expect for constructor parameters
        final HashSet<String> constructorParameterSet = new HashSet<String>(constructorParameters);
        final List<Property> setterProperties = new ArrayList<Property>();
        for (Property p : properties) {
            if (!constructorParameterSet.contains(p.name)) {
                try {
                    final Method setter = klass.getMethod("set" + Character.toUpperCase(p.fieldName.charAt(0)) + p.fieldName.substring(1));
                    setterProperties.add(new Property(setter, p.name, p.fieldName));
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Can't find setter method for parameter '" + p.name + "'");
                }

            }
        }
        // finished
        this.getter = properties.toArray(new Property[properties.size()]);
        this.setter = setterProperties.toArray(new Property[setterProperties.size()]);
    }

    public <G,D,L> T construct(ParameterHelper helper, DataDocument<G, D, L> document, D dictionary) {
        if (constructorParameters.length>0) {
            final Object[] parameters = new Object[constructorParameters.length];
            for (int i=0; i < parameters.length; ++i) {
                final String name = constructorParameters[i];
                final Object value = helper.unwrap(document, document.getFromDictionary(dictionary, name));
                parameters[i] = value;
            }
            try {
                return constructor.newInstance(parameters);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                return klass.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Parameterized delegateFor(final T object) {
        return new Parameterized() {
            @Override
            public <G, D, L> void importParameters(ParameterHelper helper, DataDocument<G, D, L> document, D dictionary) {
                for (Property set : setter) {
                    final Object value = helper.unwrap(document, document.getFromDictionary(dictionary, set.name));
                    set.set(object, value);
                }
            }

            @Override
            public <G, D, L> void exportParameters(ParameterHelper helper, DataDocument<G, D, L> document, D dictionary) {
                for (Property getter : getter) {
                    document.addToDictionary(dictionary, getter.name, helper.wrap(document, getter.get(object)) );
                }
            }
        };
    }

    private static final class Property {
        private Method method;
        private String name;
        private String fieldName;

        private Property(Method method, String name, String fieldName) {
            this.method = method;
            this.name = name;
            this.fieldName = fieldName;
        }

        Object get(Object o) {
            try {
                return method.invoke(o);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        void set(Object o, Object v) {
            try {
                method.invoke(o, v);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Property property = (Property) o;

            if (!name.equals(property.name)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}
