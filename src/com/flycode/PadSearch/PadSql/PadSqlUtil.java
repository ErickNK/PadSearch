package com.flycode.PadSearch.PadSql;


import com.flycode.PadSearch.Constants.BuildingTable;
import com.flycode.PadSearch.Constants.Constants;
import com.flycode.PadSearch.Constants.OwnerTable;
import com.flycode.PadSearch.Constants.TenantTable;
import com.flycode.PadSearch.Entities.Building;
import com.flycode.PadSearch.Entities.Owner;
import com.flycode.PadSearch.Entities.Tenant;

import java.sql.ResultSet;


//TODO: make PadSqlUtil independent form the MySqlHelper class to be able to connect to any other DBMS
public class PadSqlUtil extends MySqlHelper{
    public boolean CONNECTION_STATUS = false;
//    private Object data;

    public PadSqlUtil(String Username,String Pass){
        super(Username,Pass);
        ConnectDB();
    }

    private void ConnectDB(){
        Constants constants =  new Constants();
        if(super.connectDB(constants.DATABASE_NAME)) {
            CONNECTION_STATUS = true;
        }
        else{
            CONNECTION_STATUS = false;
            System.out.println("Error in getting connection");
        }
    }

    // ***************************/
    // *          C*R*U*D        */
    // ***************************/

    //CREATE RECORDS
    public void addTenant(Tenant data){
        TenantTable tenant_table = new TenantTable();
        String sql = "INSERT INTO "+ tenant_table.TABLE_NAME+" ("
                +tenant_table.Second_Column+","
                +tenant_table.Third_Column+","
                +tenant_table.Fourth_Column+","
                +tenant_table.Fifth_Column+","
                +tenant_table.Sixth_Column+","
                +tenant_table.Seventh_Column+") VALUES ("
                +"\'"+data.getFirst()+"\',"
                +"\'"+data.getSecond()+"\',"
                +"\'"+data.getSurname()+"\',"
                +"\'"+data.getTell()+"\',"
                +"\'"+data.getNational_ID()+"\',"
                +"\'"+data.getBio()+"\');";
        super.insertRecord(sql);
    }
    public void addBuilding(Building data){
        BuildingTable buildingTable = new BuildingTable();
        String sql = "INSERT INTO "+buildingTable.TABLE_NAME+" ("
                +buildingTable.Second_Column+","
                +buildingTable.Third_Column+","
                +buildingTable.Fourth_Column+","
                +buildingTable.Fifth_Column+","
                +buildingTable.Sixth_Column+","
                +buildingTable.Seventh_Column+") VALUES ("
                +"\'"+data.getRegistration_id()+"\',"
                +"\'"+data.getName()+"\',"
                +"\'"+data.getOwner_Name()+"\',"
                +"\'"+data.getLicense()+"\',"
                +"\'"+data.getLocation()+"\',"
                +"\'"+data.getNo_of_rooms()+"\');";
        super.insertRecord(sql);
    }
    public void addOwner(Owner data){
        OwnerTable ownerTable = new OwnerTable();
        String sql = "INSERT INTO "+ownerTable.TABLE_NAME+" ("
                +ownerTable.Second_Column+","
                +ownerTable.Third_Column+","
                +ownerTable.Fourth_Column+","
                +ownerTable.Fifth_Column+","
                +ownerTable.Sixth_Column+","
                +ownerTable.Seventh_Column+","
                +ownerTable.Eight_Column + ") VALUES ("
                +"\'"+data.getFirst()+"\',"
                +"\'"+data.getSecond()+"\',"
                +"\'"+data.getSurname()+"\',"
                +"\'"+data.getNational_id()+"\',"
                +"\'"+data.getBio()+"\',"
                +"\'"+data.getTell()+"\',"
                +"\'"+data.getOwner_id()+"\');";
        super.insertRecord(sql);
    }

    //DELETE RECORDS
    public void DeleteTenant(String id){
        TenantTable tenantTable = new TenantTable();
        String sql = "DELETE FROM " + tenantTable.TABLE_NAME
                + " WHERE  id=\'" + id+"\';";
        super.deleteRecord(sql);

    }
    public void DeleteOwner(String id){
        OwnerTable table = new OwnerTable();
        String sql = "DELETE FROM " + table.TABLE_NAME
                + " WHERE  id=\'" + id+"\';";
        super.deleteRecord(sql);

    }
    public void DeleteBuilding(String id){
        BuildingTable table = new BuildingTable();
        String sql = "DELETE FROM " + table.TABLE_NAME
                + " WHERE  id=\'" + id+"\';";
        super.deleteRecord(sql);
    }

    //UPDATE RECORDS
    /**
     * Updates Tenant records.
     * NOTE: the where clause must be given in string format. If omitted then
     * all the recodes will be updated, some may be updated to empty values.
     *  @param tenant Of PadEntity class type used to parse data to table
     * @param id id of record to use in where clause.
     * */
    public void UpdateTenant(Tenant tenant, String id){
        TenantTable tenant_table = new TenantTable();
        String sql = "UPDATE "+tenant_table.TABLE_NAME
                +" SET "
                +tenant_table.Second_Column+"=\'"+tenant.getFirst()+"\', "
                +tenant_table.Third_Column+"=\'"+tenant.getSecond()+"\', "
                +tenant_table.Fourth_Column+"=\'"+tenant.getSurname()+"\', "
                +tenant_table.Fifth_Column+"=\'"+tenant.getTell()+"\', "
                +tenant_table.Sixth_Column+"=\'"+tenant.getNational_ID()+"\', "
                +tenant_table.Seventh_Column+"=\'"+tenant.getBio()+"\'"
                +" WHERE id=\'"+ id + "\';";
        super.updateTable(sql);
    }
    public void UpdateOwner(Owner owner, String id){
        OwnerTable ownerTable = new OwnerTable();
        String sql = "UPDATE "+ownerTable.TABLE_NAME
                +" SET "
                +ownerTable.Second_Column+"=\'"+owner.getFirst()+"\', "
                +ownerTable.Third_Column+"=\'"+owner.getSecond()+"\', "
                +ownerTable.Fourth_Column+"=\'"+owner.getSurname()+"\', "
                +ownerTable.Fifth_Column+"=\'"+owner.getNational_id()+"\', "
                +ownerTable.Sixth_Column+"=\'"+owner.getBio()+"\', "
                +ownerTable.Seventh_Column+"=\'"+owner.getTell()+"\',"
                +ownerTable.Eight_Column+"=\'"+owner.getOwner_id()+"\'"
                +" WHERE id=\'"+ id + "\';";
        super.updateTable(sql);
    }
    public void UpdateBuilding(Building building,String id){
        BuildingTable buildingTable = new BuildingTable();
        String sql = "UPDATE "+buildingTable.TABLE_NAME
                +" SET "
                +buildingTable.Second_Column+"=\'"+building.getRegistration_id()+"\', "
                +buildingTable.Third_Column+"=\'"+building.getName()+"\', "
                +buildingTable.Fourth_Column+"=\'"+building.getOwner_Name()+"\', "
                +buildingTable.Fifth_Column+"=\'"+building.getLicense()+"\', "
                +buildingTable.Sixth_Column+"=\'"+building.getLocation()+"\', "
                +buildingTable.Seventh_Column+"=\'"+building.getNo_of_rooms()+"\'"
                +" WHERE id=\'"+ id + "\';";
        super.updateTable(sql);
    }

    /**
     * <p>Used to select between the different PadSearch tables</p>
     * @param tableNo the table number to be parsed for retrieving the selection<br/>
     *                1-client-test<br/>
     *                2-tenant<br/>
     *                3-owner<br/>
     *                4-building
     * */
    public ResultSet SelectTable(int tableNo){
        //TODO: make code better and shorter.
        String table;
        switch (tableNo){
            case 1://client_test table
                table = "client_test";
                break;
            case 2://Tenant table
                table = "tenant";
                break;
            case 3://Owner table
                table = "owner";
                break;
            case 4://Building table
                table = "building";
                break;
            default:
                table = "client_test";
        }
        return super.selectTable(table);
    }
}
