# External modules import
import RPi.GPIO as GPIO
import time

#import pygame.mixer
#from pygame.mixer import Sound
#pygame.mixer.pre_init(frequency=44100, size=-16, channels=2, buffer=4096)
#pygame.init()
#pygame.mixer.init()
#print pygame.mixer.get_init()
# siren = Sound("siren.wav")
#siren = Sound("/home/pi/emergency005.wav")
#siren.play()

#from socketIO_client import SocketIO, LoggingNamespace
#def on_AEDstate_response(*args):
#    print('on_AEDstate_response', args)

#socketIO = SocketIO('ec2-52-28-101-65.eu-central-1.compute.amazonaws.com',
#    3000, LoggingNamespace)
#socketIO.on('AEDstate', on_AEDstate_response)
#socketIO.emit('AEDstate')
#socketIO.wait(seconds=10)

#exit()

import os
import signal

#os.system('omxplayer -o local /home/pi/police_s.wav')
#os.spawnl(os.P_NOWAIT, 'omxplayer -o local -b /home/pi/police_s.wav')

import subprocess

import urllib2

while (True):
  response = urllib2.urlopen('http://188.130.155.238/api/AED.ashx')
  html = response.read()
  print(html)
  if int(html):

      p = subprocess.Popen(["omxplayer -o local --loop /home/pi/police_s.wav"], 
          stdout=subprocess.PIPE, shell=True, preexec_fn=os.setsid)
      #time.sleep(5)
    
      #exit()

      # Pin Definitions:
      E1 = 5
      M1 = 4
      E2 = 6
      M2 = 7

      dc = 95 # duty cycle (0-100) for PWM pin

      # Pin Setup:
      GPIO.setmode(GPIO.BCM) # Broadcom pin-numbering scheme
      GPIO.setup(M1, GPIO.OUT) # M1 pin set as output
      GPIO.setup(E1, GPIO.OUT) # E1 pin set as output
      GPIO.setup(M2, GPIO.OUT) # M2 pin set as output
      GPIO.setup(E2, GPIO.OUT) # E2 pin set as output

      pwm_E1 = GPIO.PWM(E1, 50) # Initialize pwm_E1 on E1 pin 100Hz frequency
      pwm_E2 = GPIO.PWM(E2, 50) # Initialize pwm_E2 on E2 pin 100Hz frequency

      # Initial state for the motors:
      GPIO.output(M1, GPIO.LOW)
      GPIO.output(M2, GPIO.LOW)

      pwm_E1.start(dc)
      pwm_E2.start(dc)

      print("Here we go! Press Ctrl+C to exit")

      for x in xrange(0, 50):
          # pwm_E1.ChangeDutyCycle(dc)
          GPIO.output(M1, GPIO.HIGH)
          GPIO.output(M2, GPIO.HIGH)
          time.sleep(0.1)
          GPIO.output(M1, GPIO.LOW)
          GPIO.output(M2, GPIO.LOW)
          time.sleep(0.1)
          print(x)

          # Stop PWM:
          pwm_E1.stop()
          pwm_E2.stop()

      # cleanup all GPIO
      GPIO.cleanup()

      os.killpg(os.getpgid(p.pid), signal.SIGTERM)
