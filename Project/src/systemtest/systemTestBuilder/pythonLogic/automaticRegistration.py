import os


s1 = '''package de.unisaarland.cs.se.selab.systemtest;

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestManager;

final class SystemTestsRegistration {

    private SystemTestsRegistration() {
            // empty
    }

    static void registerSystemTests(final SystemTestManager manager) {
'''
s3 = '''
    }
}
'''

def recursive_file_finder(parent_path):
	startlist = []
	for potential_directory in os.listdir(parent_path):
		if not ('.' in potential_directory):
			startlist += recursive_file_finder(f'{parent_path}/{potential_directory}')
		else:
			startlist.append(potential_directory)
	return startlist

def run(path, doRegisterPath = 'doRegister.patch'):

	with open(doRegisterPath, 'r') as file:
		lines = file.read().split('\n')

	s2List = []
	pathFileList = recursive_file_finder(path)
	pathFileList.remove('SystemTestsRegistration.java')
	pathFileList.remove('HelperClass.java')
	pathFileList.remove('ParserInvocation.java')
	doubletab = '        '

	for line in lines:
		if line[0] == '+':
			try:
				pathFileList.remove(f'{line.lstrip("+").strip()}.java')
				s2List.append(f'{doubletab}manager.registerTest(new {line.lstrip("+").strip().rstrip(".java")}());\n')
			except:
				print(f'could find no systemtest from referral : {line}')
		elif line[0] == '-':
			try:
				pathFileList.remove(f'{line.lstrip("-").strip()}.java')
			except:
				print(f'could find no systemtest from referral : {line}')

	s2List += [f'{doubletab}manager.registerTest(new {file.rstrip(".java")}());\n' for file in pathFileList]

	s2 = ''.join(s2List)

	with open(path+'/SystemTestsRegistration.java', 'w') as file:
		file.write(''.join([s1, s2, s3]))
