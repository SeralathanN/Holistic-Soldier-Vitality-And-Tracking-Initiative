import joblib
import numpy as np

# Load the trained model
model = joblib.load('rf_model.pkl')

def get_alert(sensor_data):
    # Example sensor_data: {
    #     "atmosphericTemperature": 41,
    #     "atmosphericHumidity": 88,
    #     "bodyTemperature": 40.2,
    #     "heartRate": 132
    # }
    X_input = np.array([[sensor_data['atmosphericTemperature'],
                         sensor_data['atmosphericHumidity'],
                         sensor_data['bodyTemperature'],
                         sensor_data['heartRate']]])

    label = model.predict(X_input)[0]
    return label

from flask import Flask, request, jsonify
import joblib
import numpy as np

app = Flask(__name__)

# Load the trained model
model = joblib.load('rf_model.pkl')

# Alert messages dictionary
alert_messages = {
    0: "✅ Status Normal – No action needed.",
    1: "⚠️ Health Alert – High heart rate or body temperature.",
    2: "🌡️ Environmental Alert – High heat or humidity.",
    3: "🛑 Low Heart Rate – Possible unconsciousness!",
    4: "🚨 Critical Condition – Immediate evacuation required!"
}

@app.route('/predict', methods=['POST'])
def predict():
    data = request.json

    try:
        # Extract features in correct order
        features = [
            data["atmosphericTemperature"],
            data["atmosphericHumidity"],
            data["bodyTemperature"],
            data["heartRate"]
        ]

        # Predict label
        label = model.predict([features])[0]
        message = alert_messages.get(label, "Unknown Status")

        response = {
            "status": "success",
            "label": int(label),
            "message": message
        }

        return jsonify(response), 200

    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 400

if __name__ == '__main__':
    app.run(debug=True, host="0.0.0.0", port=5000)
