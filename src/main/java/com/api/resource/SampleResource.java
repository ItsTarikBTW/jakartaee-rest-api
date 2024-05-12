package com.api.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.PathParam;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.api.Model.Address;
import com.api.Model.Carnet;

import java.io.IOException;

import java.io.FileReader;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
@Path("carnets")
public class SampleResource {
    static Carnet[] carnets;

    static {
        JsonArray addressData = SampleResource.getDataFromFile("address.json");
        JsonArray namesData = SampleResource.getDataFromFile("names.json");
        JsonArray carnetsData = SampleResource.getDataFromFile("carnets.json");

        carnets = new Carnet[4];
        for (int i = 0; i < 4; i++) {
            String carnetName = carnetsData.getJsonObject(i).getString("name");
            carnets[i] = new Carnet(i,carnetName);
        }

        int addressIndex = 0;
        for (int i = 0; i < carnets.length; i++) {
            Carnet carnet = carnets[i];
            int numAddresses = (int) Math.round(Math.random() * 4) + 1; 
            for (int j = 0; j < numAddresses; j++) {
                if (addressIndex < addressData.size()) {
                    JsonObject addressObject = addressData.getJsonObject(addressIndex);
                    Address address = new Address();
                    address.setName(addressObject.getString("name"));
                    address.setNumRue(addressObject.getInt("numRue"));
                    address.setVille(addressObject.getString("ville"));

                    String carnetName = namesData.getJsonObject(i+j).getString("name");

                    carnet.enregistrer(carnetName, address);
                    addressIndex++;
                }
            }
        }
    }
    
    public static JsonArray getDataFromFile(String fileName) {
        JsonArray jsonArray = null;
        try (FileReader fileReader = new FileReader("data/" + fileName);
             JsonReader jsonReader = Json.createReader(fileReader)) {
            jsonArray = jsonReader.readArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    @GET
    @Produces(TEXT_PLAIN)
    public static String message() {
        StringBuilder sb = new StringBuilder();
        for (Carnet carnet : carnets) {
            sb.append(carnet.toString());
        }
        return sb.toString();
    }

    @POST
    @Path("{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static Response message(@PathParam("id") String id, 
                                  @FormDataParam("nameP") String nameP, 
                                  @FormDataParam("nameA") String nameA, 
                                  @FormDataParam("numRue") int numRue, 
                                  @FormDataParam("ville") String ville) {
        int carnetId = Integer.parseInt(id);
        
        Address address = new Address();
        address.setName(nameA);
        address.setNumRue(numRue);
        address.setVille(ville);
        carnets[carnetId].enregistrer(nameP, address);
    
        return Response.ok(carnets[carnetId]).build();
    }

    @DELETE
    @Path("{id}/address/{nameP}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response delAddress(@PathParam("id") String id,
                                  @PathParam("nameP") String nameP){
        int carnetId = Integer.parseInt(id);
        carnets[carnetId].effacer(nameP);
        return Response.ok(carnets[carnetId]).build();
    }

    @GET
    @Path("{id}/address/{nameP}")
    @Produces(MediaType.APPLICATION_XML)
    public static Address getAddress(@PathParam("id") String id,
                                  @PathParam("nameP") String nameP){
        int carnetId = Integer.parseInt(id);
        Address address = carnets[carnetId].chercher(nameP);
        if (address == null) {
            throw new WebApplicationException("Address not found", 404);
        }
        return address;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Response getCarnet(@PathParam("id") String id){
        int carnetId = Integer.parseInt(id);
        return Response.ok(carnets[carnetId]).build();
    }

}