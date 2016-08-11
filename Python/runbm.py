


import glob
import os
import shutil

from rdflib import Graph



'''


#from os import makedirs, path


Stores supported:
http://rdflib.readthedocs.io/en/stable/persistence.html
http://rdflib.readthedocs.io/en/stable/persistence.html#stores-currently-shipped-with-core-rdflib
https://github.com/RDFLib/rdflib/blob/d7013fbfbb91a630d5dec1b88ab8201edae4341f/docs/plugin_stores.rst#id6+

open()-not needed for memory/default

'''




# database folder
dbdir = 'graph/'

# database type
# sleepycat (hdd) or iomemory (in-memory)
dbtype = 'IOMemory' 

# data5k.nt (5k products) or demo.nt (2 triples)
datafile = 'data5k.nt'  

# query folder
# has to end with a /
querypath = "queries5kedit/"

outputdir = "results5kedit/"


def initDir(dir):
    if os.path.exists(dir) == True:
        # remove directory
        shutil.rmtree(dir)
        # create it again
        os.makedirs(dir)


# create the graph
g = Graph(dbtype)
if (dbtype.lower() == 'sleepycat'):
    g.open(configuration=dbdir, create=True)

# load the dataset
g.parse(datafile, format='nt', location=dbdir)

triples = len(g) 
print str(triples) + ' triples loaded'

# load file names in query directory
fileList = glob.glob(querypath + '*')

qnum = 0

# read query files
for fpath in fileList:
    with open(fpath, 'r') as f:
        queryStr = f.read()
        qnum = qnum + 1
        
        # run this query
        try:    
            qres = g.query(queryStr)
        except Exception as e:
            print e
            continue
        
        # print the results into a new file
        rescount = 0
        resfile = outputdir + 'query' + str(qnum) + '.txt'
        with open(resfile, 'w') as f_out:
            # for each query result
            for row in qres:
                f_out.write(str(row))
                rescount = rescount + 1
        
        print 'Query ' + str(qnum) + ' returned ' + str(rescount) + ' results'
        

g.close()