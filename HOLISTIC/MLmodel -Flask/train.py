import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report
import joblib

def train_model(csv_file='soldier_data.csv', model_file='rf_model.pkl'):
    data = pd.read_csv(csv_file)

    # Updated input features
    X = data[['atmosphericTemperature', 'atmosphericHumidity', 'bodyTemperature', 'heartRate']]
    y = data['Label']

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

    model = RandomForestClassifier(n_estimators=100, random_state=42)
    model.fit(X_train, y_train)

    y_pred = model.predict(X_test)
    print("\n📊 Classification Report:\n")
    print(classification_report(y_test, y_pred))

    joblib.dump(model, model_file)
    print(f"✅ Model saved as '{model_file}'")

if __name__ == '__main__':
    train_model()