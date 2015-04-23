package de.unijena.bioinf.babelms.json;

import de.unijena.bioinf.ChemistryBase.chem.Ionization;
import de.unijena.bioinf.ChemistryBase.chem.PeriodicTable;
import de.unijena.bioinf.ChemistryBase.ms.CollisionEnergy;
import de.unijena.bioinf.ChemistryBase.ms.Peak;
import de.unijena.bioinf.ChemistryBase.ms.ft.RecalibrationFunction;
import de.unijena.bioinf.ChemistryBase.ms.ft.Score;
import de.unijena.bioinf.ChemistryBase.ms.ft.TreeScoring;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

class FTSpecials {

    static final HashMap<Class<Object>, Handler<Object>> defaultHandlers = getDefaultHandlers();

    protected interface Handler<T> {
        public void writeJSON(JSONWriter writer, T obj) throws JSONException;
        public T readJSON(JSONObject obj) throws JSONException;
    }

    static boolean writeSpecialAnnotation(JSONWriter writer, Class<Object> klass, Object object) throws JSONException {
        if (defaultHandlers.containsKey(klass)) {
            defaultHandlers.get(klass).writeJSON(writer, object);
            return true;
        } else return false;
    }

    static <T> T readSpecialAnnotation(JSONObject json, Class<T> klass) throws JSONException {
        if (defaultHandlers.containsKey(klass)) {
            return (T)defaultHandlers.get(klass).readJSON(json);
        } else return null;
    }

    private static HashMap<Class<Object>, Handler<Object>> getDefaultHandlers() {
        final HashMap<Class<Object>, Handler<Object>> handlers = new HashMap<Class<Object>, Handler<Object>>();

        // ionization
        add(handlers, Ionization.class, new Handler<Ionization>() {
            @Override
            public void writeJSON(JSONWriter writer, Ionization obj) throws JSONException {
                writer.key("ion");
                writer.value(obj.toString());
            }

            @Override
            public Ionization readJSON(JSONObject obj) throws JSONException {
                if (!obj.has("ion")) return null;
                return PeriodicTable.getInstance().ionByName(obj.getString("ion"));
            }
        });

        add(handlers, TreeScoring.class, new Handler<TreeScoring>() {
            @Override
            public void writeJSON(JSONWriter writer, TreeScoring obj) throws JSONException {
                writer.key("score");
                writer.object();
                writer.key("total");
                writer.value(obj.getOverallScore());
                writer.key("recalibrationBonus");
                writer.value(obj.getRecalibrationBonus());
                double addScore=0d;
                for (Map.Entry<String, Double> special : obj.getAdditionalScores().entrySet()) {
                    writer.key(special.getKey());
                    writer.value(special.getValue().doubleValue());
                    addScore += special.getValue();
                }
                writer.key("tree");
                writer.value(obj.getOverallScore()-addScore);
                writer.endObject();

            }

            @Override
            public TreeScoring readJSON(JSONObject obj) throws JSONException {
                if (obj.has("score")) {
                    final TreeScoring scoring = new TreeScoring();
                    scoring.setOverallScore(obj.getDouble("score"));
                    scoring.setRecalibrationBonus(obj.getDouble("recalibrationBonus"));
                    return scoring;
                } else return null;
            }
        });

        add(handlers, RecalibrationFunction.class, new Handler<RecalibrationFunction>() {

            @Override
            public void writeJSON(JSONWriter writer, RecalibrationFunction obj) throws JSONException {
                writer.key("recalibration");
                writer.value(obj.toString());
            }

            @Override
            public RecalibrationFunction readJSON(JSONObject obj) throws JSONException {
                if (obj.has("recalibration")) {
                    return RecalibrationFunction.fromString(obj.getString("recalibration"));
                } else return null;
            }
        });

        add(handlers, Peak.class, new Handler<Peak>() {
            @Override
            public void writeJSON(JSONWriter writer, Peak obj) throws JSONException {
                writer.key("mz");
                writer.value(obj.getMass());
                writer.key("intensity");
                writer.value(obj.getIntensity());
            }

            @Override
            public Peak readJSON(JSONObject obj) throws JSONException {
                if (obj.has("mz") && obj.has("intensity")) {
                    return new Peak(obj.getDouble("mz"), obj.getDouble("intensity"));
                } else return null;
            }
        });

        add(handlers, CollisionEnergy[].class, new Handler<CollisionEnergy[]>() {
            @Override
            public void writeJSON(JSONWriter writer, CollisionEnergy[] obj) throws JSONException {
                writer.key("collisionEnergies");
                writer.array();
                for (CollisionEnergy e : obj) writer.value(e.toString());
                writer.endArray();
            }

            @Override
            public CollisionEnergy[] readJSON(JSONObject obj) throws JSONException {
                if (!obj.has("collisionEnergies")) return null;
                final JSONArray jary = obj.getJSONArray("collisionEnergies");
                final CollisionEnergy[] ary = new CollisionEnergy[jary.length()];
                for (int k=0; k < ary.length; ++k) {
                    ary[k] = CollisionEnergy.fromString(jary.getString(k));
                }
                return ary;
            }
        });

        add(handlers, Score.class, new Handler<Score>() {
            private WeakHashMap<String[], String[]> constantPool = new WeakHashMap<String[], String[]>();
            @Override
            public void writeJSON(JSONWriter writer, Score obj) throws JSONException {
                writer.key("score");
                writer.value(obj.sum());
                writer.key("scores");
                writer.object();
                for (int k=0; k < obj.size(); ++k) {
                    writer.key(obj.getScoringMethod(k));
                    writer.value(obj.get(k));
                }
                writer.endObject();
            }

            @Override
            public Score readJSON(JSONObject obj) throws JSONException {
                if (obj.has("score") && obj.has("scores")) {
                    final JSONObject jary = obj.getJSONObject("scores");
                    String[] names = JSONObject.getNames(jary);
                    synchronized (constantPool) {
                        final String[] replace = constantPool.get(names);
                        if (replace!=null) names = replace;
                        else constantPool.put(names, names);
                    }
                    final Score s = new Score(names);
                    for (int k=0; k < names.length; ++k) {
                        s.set(k, jary.getDouble(names[k]));
                    }
                    return s;
                } else return null;
            }
        });

        return handlers;
    }

    private static <T> void add(HashMap<Class<Object>, Handler<Object>> set, Class<T> klass, Handler<T> handler) {
        set.put((Class<Object>)klass, (Handler<Object>)handler);
    }

}
