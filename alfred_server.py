from flask import Flask
import RPi.GPIO as GPIO
import time

GPIO.setwarnings(False)

app = Flask(__name__)

PIN = 16

@app.route('/lights_off')
def on():
    GPIO.setmode(GPIO.BOARD)
    GPIO.setup(PIN, GPIO.OUT)
    return '200'

@app.route('/lights_on')
def off():
    GPIO.cleanup()
    return '200'

app.run('', 1995)

