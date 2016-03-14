package org.neo4j.neo4j;

import java.util.Iterator;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.kernel.impl.util.StringLogger;

/**
 * @author Archer Yu
 * @since 20160131
 */



public class Neo4jSampleCode {
	
	public enum NodeType implements Label{ //Label Enumeration
		Person, Course;
	}
	
	public enum RelationType implements RelationshipType{ //RelationshipType Enumeration
		Knows, BelongsTo;
	}
	
    public static void main( String[] args )
    {
    	GraphDatabaseFactory gdbFactory = new GraphDatabaseFactory();
    	GraphDatabaseService gdbService = gdbFactory.newEmbeddedDatabase("C:\\neo4j-community-2.0.4\\data\\graph.db");//DB Pass
    	
    	ExecutionEngine execEngine = new ExecutionEngine(gdbService, StringLogger.SYSTEM);
    	ExecutionResult execResult;
    	
    	try(Transaction tx = gdbService.beginTx()){ //beginTransaction
    		
    		//initial clear all
    		
    		execEngine.execute("MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n, r");
    		
    		//create NodeType Person node 
    		Node waldoNode = gdbService.createNode(NodeType.Person);
    		waldoNode.setProperty("PID", 426);
    		waldoNode.setProperty("Name", "Waldo");
    		waldoNode.setProperty("Age", 78);
    		
    		Node archerNode = gdbService.createNode(NodeType.Person);
    		archerNode.setProperty("PID", 689);
    		archerNode.setProperty("Name", "Archer");
    		
    		Node troyNode = gdbService.createNode(NodeType.Person);
    		troyNode.setProperty("Name", "Troy");

    		//create NodeType Course node
    		Node neo4jItrodNode = gdbService.createNode(NodeType.Course);
    		neo4jItrodNode.setProperty("Id", 1);
    		neo4jItrodNode.setProperty("Name", "Neo4j Introduction");
    		neo4jItrodNode.setProperty("Location", "Room 7F-3");
    		
    		Node jcsAdvancedNode = gdbService.createNode(NodeType.Course);
    		jcsAdvancedNode.setProperty("Name", "JCS Advanced");
    		
    		//create Relation node
    		waldoNode.createRelationshipTo(archerNode, RelationType.Knows);
    		
    		Relationship bobRelIt = waldoNode.createRelationshipTo(neo4jItrodNode, RelationType.BelongsTo);
    		bobRelIt.setProperty("Function", "Student");
    		
    		Relationship bobRelElectronics = waldoNode.createRelationshipTo(jcsAdvancedNode, RelationType.BelongsTo);
    		bobRelElectronics.setProperty("Function", "Supply Teacher");
    		
    		Relationship aliceRelIt = archerNode.createRelationshipTo(neo4jItrodNode, RelationType.BelongsTo);
    		aliceRelIt.setProperty("Function", "Teacher");
    		
    		tx.success(); //set Transaction success
    		
    		//Print Result we execute in up
    		execResult = execEngine.execute("match (n:Person) return n");
    		Iterator<Node> columns =execResult.columnAs("n");
    		
    		for(Node node : IteratorUtil.asIterable(columns)){
    			System.out.println(node + ":Name="+ node.getProperty("Name"));    			
    		}
    	}
    	
    	gdbService.shutdown();//close Service Connection

    }
}
