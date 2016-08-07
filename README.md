# S2RDF_BSBM
BSBM version of the S2RDF benchmark

1. Data and query generation is done with the bsbsmtool.

2. The DataSetCreator generates the VP and ExtVP tables.

3. SPARQL-Queries are translated using the S2RDF QueryTranslator.

4. For the execution of the queries JDBC4RDF is used. --> https://github.com/mxhdev/JDBC4RDF

All this is done in the forked Aloja version to get the performance data. --> https://github.com/mxhdev/aloja