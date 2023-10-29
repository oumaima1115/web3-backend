/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo;

import java.io.File;

import java.io.InputStream;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.util.FileManager;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author DO.ITSUDPARIS
 */
public class JenaEngine {

    static private String RDF = "http://www.semanticweb.org/hp/ontologies/2022/9/untitled-ontology-12#";

    /**
     * Charger un mod�le � partir d�un fichier owl
     *
     * @param args + Entree: le chemin vers le fichier owl
     *             + Sortie: l'objet model jena
     */
    static public Model readModel(String inputDataFile) {
// create an empty model
        Model model = ModelFactory.createDefaultModel();

// use the FileManager to find the input file
        InputStream in = FileManager.get().open(inputDataFile);
        if (in == null) {
            System.out.println("Ontology file: " + inputDataFile + "not found");
            return null;
        }
// read the RDF/XML file
        model.read(in, "");

        try {
            in.close();
        } catch (IOException e) {
// TODO Auto-generated catch block
            return null;
        }
        return model;
    }

    /**
     * Faire l'inference
     *
     * @param args + Entree: l'objet model Jena avec le chemin du fichier de
     *             regles
     *             + Sortie: l'objet model infere Jena
     */
    static public Model readInferencedModelFromRuleFile(Model model,
                                                        String inputRuleFile) {
        InputStream in = FileManager.get().open(inputRuleFile);
        if (in == null) {
            System.out.println("Rule File: " + inputRuleFile + " not found");
            return null;
        } else {
            try {
                in.close();
            } catch (IOException e) {
// TODO Auto-generated catch block
                return null;
            }
        }
        List rules = Rule.rulesFromURL(inputRuleFile);
        GenericRuleReasoner reasoner = new
                GenericRuleReasoner(rules);
        reasoner.setDerivationLogging(true);
        reasoner.setOWLTranslation(true); // not needed in RDFS case
        reasoner.setTransitiveClosureCaching(true);
        InfModel inf = ModelFactory.createInfModel(reasoner, model);
        return inf;
    }

    /**
     * Executer une requete
     *
     * @param args + Entree: l'objet model Jena avec une chaine des caracteres
     *             SparQL
     *             + Sortie: le resultat de la requete en String
     */
    static public OutputStream executeQuery(Model model, String
            queryString) {
        Query query = QueryFactory.create(queryString);
// No reasoning
// Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query,

                model);
        ResultSet results = qe.execSelect();
        OutputStream output = new OutputStream() {
            private StringBuilder string = new StringBuilder();

            public void write(int b) throws IOException {
                this.string.append((char) b);
            }

            //Netbeans IDE automatically overrides this toString()
            public String toString() {
                return this.string.toString();
            }
        };
        ResultSetFormatter.outputAsJSON(output, results);
        return output;
    }

    /**
     * Executer un fichier d'une requete
     *
     * @param args + Entree: l'objet model Jena avec une chaine des caracteres
     *             SparQL
     *             + Sortie: le resultat de la requete en String
     */
    static public OutputStream executeQueryFile(Model model, String
            filepath) {
        File queryFile = new File(filepath);
// use the FileManager to find the input file
        InputStream in = FileManager.get().open(filepath);
        if (in == null) {
            System.out.println("Query file: " + filepath + " not found");
            return null;
        } else {
            try {
                in.close();
            } catch (IOException e) {
// TODO Auto-generated catch block
                return null;
            }
        }
        String queryString = FileTool.getContents(queryFile);
        return executeQuery(model, queryString);
    }


    static public OutputStream executeQueryFileParams(Model model, String filepath, String params) {
        File queryFile = new File(filepath);
        InputStream in = FileManager.get().open(filepath);
        if (in == null) {
            System.out.println("Query file: " + filepath + " not found");
            return null;
        } else {
            try {
                in.close();
            } catch (IOException e) {
                return null;
            }
        }
        String queryString = FileTool.getContents(queryFile);
        queryString = String.format(queryString, params); // Replace %s with the actual params value
        return executeQuery(model, queryString);
    }
}