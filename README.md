Raft
====
The implementation of RAFT is for making DB changes across all the Distributed systems. 
The DB transactions will be basically a CRUD operations, so the INSERT, UPDATE, DELETE and SELECT queries used in 
the transaction can be send to other systems for updating their transaction status.

This project sends the SQL Query with their values in the XML Stream to the distributed systems 
and the XML Stream is read and persisted and it will be processed and updates the DB offline.
