import os


path = '../../java/de/unisaarland/cs/se/selab/systemtest'

def recursive_file_finder(parent_path):
  startlist = []
  for potential_directory in os.listdir(parent_path):
    if not ('.' in potential_directory):
      startlist += recursive_file_finder(f'{parent_path}/{potential_directory}')
    else:
      startlist.append(potential_directory)
  return startlist

print(recursive_file_finder(path))