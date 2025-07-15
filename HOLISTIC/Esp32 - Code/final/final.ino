#include <TinyGPSPlus.h>
#include <HardwareSerial.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include <DHT.h>
#include <OneWire.h>
#include <DallasTemperature.h>

// GPS Module (BN-880)
static const int RXPin = 16, TXPin = 17;
static const uint32_t GPSBaud = 9600;
TinyGPSPlus gps;
HardwareSerial gpsSerial(2);

// DHT11 Sensor (Atmospheric Temperature & Humidity)
#define DHTPIN 25
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

// DS18B20 Sensor (Body Temperature)
#define ONE_WIRE_BUS 26
OneWire oneWire(ONE_WIRE_BUS);
DallasTemperature ds18b20(&oneWire);

// HW-827 Heart Rate Sensor (Analog)
#define HEART_SENSOR_PIN 34

// Wi-Fi credentials
const char* ssid = "POCO M4 pRO";     //BSNL-BHARAT FIBER
const char* password = "ppooppoo";  //abc2998218

// Backend server URL
const char* serverUrl = "http://192.168.1.4:8080/api/data";

void setup() {
  Serial.begin(115200);
  gpsSerial.begin(GPSBaud, SERIAL_8N1, RXPin, TXPin);

  // Initialize sensors
  dht.begin();
  ds18b20.begin();
  pinMode(HEART_SENSOR_PIN, INPUT);

  // Connect to Wi-Fi
  connectToWiFi();
}

void loop() {
  smartDelay(500); // Read GPS data
  
  if (WiFi.status() == WL_CONNECTED) {
    // Read sensor data
    float atmosphericTemperature = dht.readTemperature();
    float atmosphericHumidity = dht.readHumidity();
    ds18b20.requestTemperatures();
    float bodyTemperature = ds18b20.getTempCByIndex(0);
    int heartRate = analogRead(HEART_SENSOR_PIN);

    // Read GPS data
    float latitude = gps.location.isValid() ? gps.location.lat() : 0.0;
    float longitude = gps.location.isValid() ? gps.location.lng() : 0.0;
    float altitude = gps.altitude.isValid() ? gps.altitude.meters() : 0.0;
    float speed = gps.speed.isValid() ? gps.speed.kmph() : 0.0;

    // Print sensor & GPS data to Serial Monitor
    Serial.println("Sensor & GPS Data:");
    Serial.printf("Lat: %.6f, Lon: %.6f, Alt: %.2f m, Speed: %.2f km/h\n", latitude, longitude, altitude, speed);
    Serial.printf("Temp: %.2f°C, Humidity: %.2f%%, Body Temp: %.2f°C, Heart Rate: %d\n", atmosphericTemperature, atmosphericHumidity, bodyTemperature, heartRate);
    
    // Create JSON payload
    String payload = "{";
    payload += "\"latitude\":" + String(latitude) + ",";
    payload += "\"longitude\":" + String(longitude) + ",";
    payload += "\"altitude\":" + String(altitude) + ",";
    payload += "\"speed\":" + String(speed) + ",";
    payload += "\"atmosphericTemperature\":" + String(atmosphericTemperature) + ",";
    payload += "\"atmosphericHumidity\":" + String(atmosphericHumidity) + ",";
    payload += "\"bodyTemperature\":" + String(bodyTemperature) + ",";
    payload += "\"heartRate\":" + String(heartRate);
    payload += "}";

    // Print payload
    Serial.println("Payload: " + payload);

    // Send HTTP POST request
    HTTPClient http;
    http.begin(serverUrl);
    http.addHeader("Content-Type", "application/json");
    int httpResponseCode = http.POST(payload);
    Serial.printf("HTTP Response code: %d\n", httpResponseCode);
    http.end();
  } else {
    Serial.println("WiFi disconnected");
  }

  delay(5000); // Send data every 5 seconds
}

// Function to connect to Wi-Fi
void connectToWiFi() {
  Serial.print("Connecting to WiFi");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print(".");
  }
  Serial.println("\nConnected to WiFi. Local IP: ");
  Serial.println(WiFi.localIP());
}

// Smart delay function to process GPS data
static void smartDelay(unsigned long ms) {
  unsigned long start = millis();
  while (millis() - start < ms) {
    while (gpsSerial.available()) {
      gps.encode(gpsSerial.read());
    }
  }
}