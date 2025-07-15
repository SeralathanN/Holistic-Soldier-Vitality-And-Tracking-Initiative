import random
import csv

def generate_dummy_data(filename='soldier_data.csv', rows=1000):
    with open(filename, mode='w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(["atmosphericTemperature", "atmosphericHumidity", "bodyTemperature", "heartRate", "Label"])

        for _ in range(rows):
            atm_temp = random.randint(15, 45)
            humidity = random.randint(30, 95)
            body_temp = round(random.uniform(35.5, 41.5), 1)
            heart_rate = random.randint(60, 150)

            if heart_rate > 130 or body_temp > 40:
                label = 4
            elif body_temp > 38 or heart_rate > 110:
                label = 1
            elif atm_temp > 40 or humidity > 85:
                label = 2
            elif heart_rate < 70:
                label = 3
            else:
                label = 0

            writer.writerow([atm_temp, humidity, body_temp, heart_rate, label])

    print(f"✅ {rows} rows saved to '{filename}'")

if __name__ == '__main__':
    generate_dummy_data()