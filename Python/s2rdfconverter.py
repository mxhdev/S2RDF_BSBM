import glob
import time

from sys import argv, exit, stdout
from os import path

VERSION = "1.0.0"

"""
This script takes a ntriples file and converts it to a 
tab-separated csv-like file (TSV)
All predicates will be shortened to 20 characters

Input File:
<s> <p> <o>
<s2> <p2> "String Literal"^^<TypeObject>

Output File:
s   p   o
s2   p2   "String Literal"^^TypeObject

"""


# help string
HELP_STRING = """\nUsage:\n
-fp PathToFile.nt
"""






def getOpts(argv):
    opts = {}
    
    if (len(argv) < 3):
        for arg in argv:
            opts[arg] = ""
        return opts

    while argv:
        if argv[0][0] == '-':
            opts[argv[0]] = argv[1]
            argv = argv[2:]
        else:
            argv = argv[1:]
    return opts




def parseTriple(tripleStr):
    triple = ['', '', '']
    tripleStr = tripleStr.strip()
    pos = 0
    for tchar in tripleStr:
        if (tchar == ' '):
            # new triple maybe?
            if (pos > 1):
                # string literals might contain spaces
                triple[pos] = triple[pos] + tchar
            else:
                if (triple[pos] != ''):
                    # current triple element already read
                    pos = pos + 1
                else:
                    # start of a triple?
                    triple[pos] = triple[pos] + tchar
        else:
            triple[pos] = triple[pos] + tchar
    
    # remove final . of last triple
    triple[2] = triple[2][0:triple[2].rfind('.')]

    # remove leading/trailing spaces from all triples
    for i in range(len(triple)):
        t = triple[i]
        t = t.strip()
        triple[i] = t
    
    # deal with triples which now have the shape
    # "ab"^^<http://www.example.org
    obj = triple[2]
    delim = '^^<'
    # # split at the right most position
    idx = obj.rfind(delim)
    # re-concatenate string if needed
    if (idx >= 0):
        triple[2] = obj[:idx] + delim[:-1] + obj[(idx + len(delim)):]
    triple[1] = triple[1][-20:]
    # return the final triple
    return triple




def appendTriple(file, triple):
    with open(file, 'a+') as f:
        f.write(triple + "\n")




def convertFile(fpath):
    tcounter = 0
    fout = fpath[0:fpath.rfind('.')] + '.tsv'
    with open(fpath, 'r') as f:
        for line in f:
            # for each line / triple
            triple = parseTriple(line)
            # write to file, tab separated
            output = triple[0] + "\t" + triple[1] + "\t" + triple[2]
            appendTriple(fout, output)
            tcounter = tcounter + 1

            # fancy output-counter (slows the script down)
            #stdout.write('\r' + ' '*20)
            #stdout.flush()

            #stdout.write('\rWrote ' + str(tcounter) + ' triples')
            #stdout.flush()
            
    print('\n> Done! Wrote ' + str(tcounter) + ' triples')





def main():
    opts = getOpts(argv)

    fpath = "data.nt"
    inPlace = False
    showHelp = False

    # help
    showHelp = (('-help' in opts) | ('-h' in opts) | ('-?' in opts))
    if (showHelp):
        print(HELP_STRING)
        exit()

    # file path (single file)
    try:
        fpath = opts['-fp']
    except KeyError:
        print('No file path given!')
        exit()

    try:
        inPlaceOpt = opts['-ip']
        if (inPlaceOpt.lower() in ['true', '1']):
            inPlace = True
        else:
            inPlace = False
    except KeyError:
        inPlace = False

    # do something with the user input
    if (path.isfile(fpath)):
        convertFile(fpath)
    else:
        print('Path/File \'' + fpath + '\' does not exist')
        exit()

   
if __name__ == '__main__':
    main()


