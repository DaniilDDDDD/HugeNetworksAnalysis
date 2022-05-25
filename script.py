import csv

def parse_txt(filename, oriented=True):
    """
    Parse data from txt file into dict python type.
    JSON serializable.
    """
    data = {}
    with open(filename) as file:
        
        line = file.readline()
        while line:
            
            # skip comments
            if line[0] == '#':
                line = file.readline()
                continue
            
            parent, child = line.split()
            
            parent = int(parent)
            child = int(child)
            
            # rows in data file can be duplicated
            if parent in data:
                if child not in data[parent]['linked']:
                    data[parent]['linked'].append(child)
                    data[parent]['degree'] += 1
            else:
                data[parent] = { 
                    'linked': [child],
                    'degree': 1,
                }
                
            if oriented:
                if child not in data:
                    data[child] = { 
                    'linked': [],
                    'degree': 0,
                }
                
            else:
                if child in data:
                    if parent not in data[child]['linked']:
                        data[child]['linked'].append(parent)
                        data[child]['degree'] += 1

                else:    
                    data[child] = {
                        'linked': [parent],
                        'degree': 1,
                    }

            line = file.readline()

    return data

def parse_csv(filename, oriented=True):
    data = {}
    
    with open(filename) as file:
        reader = csv.reader(file)
        next(reader)
        
        for row in reader:
            
            parent = int(row[0])
            child = int(row[1])
            
            if parent in data:
                if child not in data[parent]['linked']:
                    data[parent]['linked'].append(child)
                    data[parent]['degree'] += 1
            else:
                data[parent] = { 
                    'linked': [child],
                    'distances': {},
                    'degree': 1,
                    'centrality': 0,
                    'marked': False,
                    'active': True
                }
                
            if oriented:
                if child not in data:
                    data[child] = { 
                    'linked': [],
                    'distances': {},
                    'degree': 1,
                    'centrality': 0,
                    'marked': False,
                    'active': True
                }
                
            else:
                if child in data:
                    if parent not in data[child]['linked']:
                        data[child]['linked'].append(parent)
                        data[child]['degree'] += 1

                else:    
                    data[child] = {
                        'linked': [parent],
                        'distances': {},
                        'degree': 1,
                        'centrality': 0,
                        'marked': False,
                        'active': True
                    }
                    
    return data

def parse(filename, oriented=True):
    if filename.split('.')[-1] == 'txt':
        return parse_txt(filename, oriented)
    elif filename.split('.')[-1] == 'csv':
        return parse_csv(filename, oriented)

import sys
sys.setrecursionlimit(10000000)

FILENAME = 'web-Google.txt'
#FILENAME = 'test.txt'
ORIENTED = True
data = parse(FILENAME, ORIENTED)
def count_vertices(graph):
    return len(graph)

def count_edges(graph,oriented = False):
    edges = 0
    for item in graph.values():
        edges += item['degree']
    if oriented:
        return edges
    return edges / 2

vertices = count_vertices(data)
edges = count_edges(data,ORIENTED)
complete_graph_edges = vertices * (vertices - 1) / 2

print(f'Number of vertices in {FILENAME}: {vertices}')
print(f'Number of edges in {FILENAME}: {edges}')
print(f'Number of edges in complete graph: {complete_graph_edges}')
print(f'Density: {edges / complete_graph_edges}')

def dfs(graph, start, visited=None):
    if visited is None:
        visited = set()
    visited.add(start)
    for next in set(graph[start]["linked"]) - visited:
        dfs(graph, next, visited)
    return visited

def get_weak_connectivity_components(graph):
    weak_connectivity_components = []
    max_component = []
    nodes = []
    for item in graph.keys():
        if item not in nodes:
            component = dfs(graph,item)
            for key in component:
                nodes.append(key)
            if len(component) > len(max_component):
                max_component = component
            weak_connectivity_components.append(component)
                
    return (weak_connectivity_components, max_component)


def make_unoriented(graph):
    result = dict.fromkeys(graph.keys(),{})
    for key in graph.keys():
        result[key] = graph[key]
    for key in result.keys():
        for vertex in graph[key]['linked']:
            if key not in result[vertex]['linked'] and key != vertex:
                result[vertex]['linked'].append(key)
                result[vertex]['degree'] += 1
    return result
    
print("hello")
[weak_connectivity_components,max_weak_connectivity_component] = get_weak_connectivity_components(data)
print("world")
max_weak_connectivity_component_size = len(max_weak_connectivity_component)
print(weak_connectivity_components)
print(max_weak_connectivity_component_size)
print(f'Number of weak connectivity components in {FILENAME}: {len(weak_connectivity_components)}')
print(f'Proportion of vertices in max weak connectivity component: {max_weak_connectivity_component_size/vertices}')