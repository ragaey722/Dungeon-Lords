from pythonLogic import shortfileparser, \
  automaticRegistration
import os

path = '../java/de/unisaarland/cs/se/selab/systemtest'
doRegisterPath = 'doRegister.patch'
systemTestShortFilesPath = 'systemTestShortfiles'


for shortfile in os.listdir(systemTestShortFilesPath):
  shortfileparser.run(f'{systemTestShortFilesPath}/{shortfile}', path)

automaticRegistration.run(path, doRegisterPath)

print(f'compiled shortfiles in {systemTestShortFilesPath}, saved them in {path} and registerd all Tests specified in {doRegisterPath}')