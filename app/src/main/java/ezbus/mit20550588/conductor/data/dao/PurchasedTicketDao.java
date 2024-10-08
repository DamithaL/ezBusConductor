package ezbus.mit20550588.conductor.data.dao;
import static ezbus.mit20550588.conductor.util.Constants.Log;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ezbus.mit20550588.conductor.data.model.PurchasedTicketModel;


@Dao
public interface PurchasedTicketDao {

    // Insert a new purchased ticket
    @Insert
    void insertPurchasedTicket(PurchasedTicketModel purchasedTicketModel);


    // Get all purchased tickets
//    @Query("SELECT * FROM purchased_ticket_table ORDER BY purchasedDate DESC")
//    LiveData<List<PurchasedTicketModel>> getAllPurchasedTickets();
    @Query("SELECT * FROM purchased_ticket_table ORDER BY isRedeemed, purchasedDate DESC")
    LiveData<List<PurchasedTicketModel>> getAllPurchasedTickets();


    // Update query for updating the details of a purchased ticket
    @Update
    void updatePurchasedTicket(PurchasedTicketModel purchasedTicket);

    // Delete a purchased ticket
    @Delete
    void deletePurchasedTicket(PurchasedTicketModel purchasedTicket);

    // Add any additional queries you may need

    @Query("SELECT * FROM purchased_ticket_table WHERE orderId = :orderId LIMIT 1")
    PurchasedTicketModel getPurchasedTicketByOrderId(String orderId);


    @Query("SELECT COUNT(*) FROM purchased_ticket_table WHERE isRedeemed = 0")
    LiveData<Integer> getCountOfNewTickets();

}
