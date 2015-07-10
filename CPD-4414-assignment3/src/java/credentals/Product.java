/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author c0645457
 */
package credentals
;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author c0649623
 */

@Path("/products")
public class Product {
    
    @GET
    @Produces("application/json")
    public Response doGet() {
        return Response.ok(getResults("SELECT * FROM product"), MediaType.APPLICATION_JSON).build();
    }
    
    @GET
    @Path("{ProductID}")
    @Produces("application/json")
    public Response doGet(@PathParam("ProductID") int id) {
        return Response.ok(getResults("SELECT * FROM product WHERE ProductID = ?", String.valueOf(id)), MediaType.APPLICATION_JSON).build();
    }
    
    @POST
    @Consumes("application/json")
    public Response doPost(String data) throws SQLException {
        JsonReader reader = Json.createReader(new StringReader(data));
        JsonObject json = reader.readObject();
        Connection conn = database.getConnection();
        
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO `product`(`ProductID`, `Name`, `Description`, `Quantity`) "
               + "VALUES ("
               +"null, '"
               + json.getString("Name") + "', '"
               + json.getString("Description") +"', "
               + json.getInt("Quantity")
               +");"
        );
        try {
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            return Response.status(500).entity("500: Post error").build();
        }
        
        PreparedStatement pstmtID = conn.prepareStatement("SELECT `ProductID` FROM product ORDER BY `OroductID` DESC LIMIT 1");
        ResultSet rs = pstmtID.executeQuery();
        rs.next();
        String id = String.valueOf(rs.getInt("productID"));
        
        return Response.ok("http://localhost:8080/CPD-4414-assignment3/products/" + id + " \n" + getResults("SELECT * FROM product WHERE productID = ?", id)).build();
    }
   
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public Response doPut(@PathParam("id") int id, String data) throws SQLException {
        JsonReader reader = Json.createReader(new StringReader(data));
        JsonObject json = reader.readObject();
        Connection conn = database.getConnection();
        
        PreparedStatement pstmt = conn.prepareStatement("UPDATE `product` SET `Name`='"
                +json.getString("Name")+"',`Description`='"
                +json.getString("Description")+"',`Quantity`="
                +json.getInt("Quantity")+" WHERE `ProductID`="
                +id
        );
        try {
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            return Response.status(500).entity("500: Update error").build();
        }
        
        return Response.ok("http://localhost:8080/CPD-4414-assignment3/products/" + id + " \n" + getResults("SELECT * FROM product WHERE ProductID = " + id)).build();
    }
    
    @DELETE
    @Path("{id}")
    public Response doDelete(@PathParam("id") int id) throws SQLException {
        Connection conn = database.getConnection();
        if ("[]".equals(getResults("SELECT * FROM product WHERE productID = ?", String.valueOf(id)))) {
            return Response.status(500).entity("500: Product " + id + " does not exist.").build();
        }
        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM `products` WHERE `productID`=" + String.valueOf(id));
        
        try {
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            return Response.status(500).entity("500: Delete error").build();
            
        }
        return Response.ok("").build();
    }
    
    @DELETE
    public Response doDelete() throws SQLException {
        return Response.status(500).entity("500: Delete error. You did not enter the product id").build();
    }
    
    private String getResults(String query, String... params) {
        String result = "";
        
        try (Connection conn = database.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            
            ResultSet rs = pstmt.executeQuery();
            StringWriter out = new StringWriter();
            JsonGeneratorFactory factory = Json.createGeneratorFactory(null);
            JsonGenerator gen = factory.createGenerator(out);
            
            gen.writeStartArray();
            while (rs.next()) {
                gen.writeStartObject()
                    .write("ProductID", rs.getInt("ProductID"))
                    .write("Name", rs.getString("Name"))
                    .write("Description", rs.getString("Description"))
                    .write("Quantity", rs.getInt("Quantity"))
                    .writeEnd();
            }
            gen.writeEnd();
            gen.close();
            result = out.toString();
            
        } catch (SQLException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}