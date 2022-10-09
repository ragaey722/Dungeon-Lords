from pythonLogic.test_builder_data import *

doubletab = '        '
tab = '    '

s1 = '''package de.unisaarland.cs.se.selab.systemtest;


import de.unisaarland.cs.se.selab.comm.TimeoutException;
import de.unisaarland.cs.se.selab.systemtest.api.SystemTest;
import de.unisaarland.cs.se.selab.systemtest.api.Utils;
import java.util.Set;


'''

def naming(name, fail):
  s = f'public class {name} extends SystemTest '
  s += '{\n\n'
  s += f'{tab}{name}() '
  s += '{\n'
  s += f'{doubletab}super({name}.class, {fail});\n{tab}'
  s += '}\n'
  return s

s2 = '''

    @Override
    public String createConfig() {
'''


def config(path, name):
  return f'{doubletab}return Utils.loadResource({name}.class, "{path}");'

s3 = '''
    }

    @Override
    public long createSeed() {
'''

def seeding(seed=42):
  return f'{doubletab}return {str(seed)};'

s4 = '''
    }

    @Override
    protected Set<Integer> createSockets() {
'''

def sockets(tuple_of_sockets):
  return f'{doubletab}return Set.of{tuple_of_sockets};'

s5 = '''
    }

    @Override
    public void run() throws TimeoutException {
        final String config = createConfig();
'''

# insert formatted_run_content here

s6 = '''
    }
}
'''


tuple_of_sockets = ()
players = {}
player_counter = 0
formatted_run_content = []

def content(string):
  return [s.strip() for s in string.strip().replace(' ', '\t').split('\t')[1:]]

def argument_formatter(args):
  s = ''
  for arg in args:
    s += f'{str(arg)}, '
  return s[:-2]

def action(line):
  global player_counter
  c = content(line)
  # handle joining of player
  if action_dict[c[0]][0] == 'Register':
    players[c[1]] = player_counter
    global tuple_of_sockets
    tuple_of_sockets += (player_counter,)
    player_counter += 1

  s = f'{doubletab}this.send{action_dict[c[0]][0]}({argument_formatter([players[c[1]]]+c[2:])});\n'

  # handle leaving of player
  if action_dict[c[0]][0] == 'Leave':
    players.pop(c[1])
  return s

def event(line):
  c = content(line)
  return f'{doubletab}this.assert{event_dict[c[0]][0]}({argument_formatter([players[c[1]]]+c[2:])});\n'

def eventBroadcast(line):
  c = content(line)
  s = []
  s.append(f'{doubletab}// Broadcast begin\n')
  for player in players:
    s.append(f'{doubletab}this.assert{event_dict[c[0]][0]}({argument_formatter([players[player]]+c[1:])});\n')
  s.append(f'{doubletab}// Broadcast end\n')
  return ''.join(s)

def nothing(line):
  return '\n'

def run(filePath, path, seed = 42, config_path = "configuration.json",
    fail = 'false'):
  # read the file
  with open(filePath, 'r') as file:
    lines = file.read().split('\n')

  # process file lines
  for line in lines:
    line = line.lower().strip()
    if len(line) < 1:
      continue
    elif line[0] == '<':
      s = action(line)
    elif line[0] == '>':
      if line[1] == '>':
        s = eventBroadcast(line)
      else:
        s = event(line)
    else:
      s = nothing(line)
    formatted_run_content.append(s)

  name = f'Shortfile{filePath.rstrip("txt").split("/")[-1].rstrip(".")}'

  all_stringlist = [
    s1,
    naming(name, fail),
    s2,
    config(config_path,name),
    s3,
    seeding(seed),
    s4,
    sockets(tuple_of_sockets),
    s5,
    ''.join(formatted_run_content),
    s6
  ]

  final_string = ''.join(all_stringlist)


  with open(f'{path}/{name}.java', 'w') as file:
    file.write(final_string)


# implement assertEvent(id)