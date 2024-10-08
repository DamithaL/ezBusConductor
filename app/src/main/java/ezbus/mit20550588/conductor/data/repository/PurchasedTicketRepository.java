package ezbus.mit20550588.conductor.data.repository;

import static ezbus.mit20550588.conductor.util.Constants.Log;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ezbus.mit20550588.conductor.data.dao.PurchasedTicketDao;
import ezbus.mit20550588.conductor.data.database.AppDatabase;
import ezbus.mit20550588.conductor.data.model.PurchasedTicketModel;
import ezbus.mit20550588.conductor.data.model.RecentSearchModel;

public class PurchasedTicketRepository {
    private PurchasedTicketDao purchasedTicketDao;
    private LiveData<List<PurchasedTicketModel>> allPurchasedTickets;


    public PurchasedTicketRepository(Application application) {
        Log("PurchasedTicketRepository", "1");
        AppDatabase database = AppDatabase.getInstance(application);
        purchasedTicketDao = database.purchasedTicketDao();
        allPurchasedTickets = purchasedTicketDao.getAllPurchasedTickets();
    }

    public  LiveData<Integer> getCountOfNewTickets(){
        return purchasedTicketDao.getCountOfNewTickets();
    }
    public void insert(PurchasedTicketModel purchasedTicket) {
        Log("PurchasedTicketRepository", "2");
        InsertAsyncTask insertTask = new InsertAsyncTask(purchasedTicketDao);
        insertTask.performBackgroundTask(purchasedTicket);
    }

    public LiveData<List<PurchasedTicketModel>> getAllPurchasedTickets() {
        Log("PurchasedTicketRepository", "3");
        return allPurchasedTickets;
    }

    // Method to update the date of a purchased ticket
    public void updatePurchasedTicket(PurchasedTicketModel purchasedTicket, Date redeemedDate) {
        Log("PurchasedTicketRepository", "4");
        UpdateAsyncTask updateTask = new UpdateAsyncTask(purchasedTicketDao);
        updateTask.performBackgroundTask(redeemedDate, purchasedTicket);

    }

    private static class InsertAsyncTask {

        private Executor executor = Executors.newSingleThreadExecutor();
        private PurchasedTicketDao purchasedTicketDao;

        public InsertAsyncTask(PurchasedTicketDao purchasedTicketDao) {
            Log("PurchasedTicketRepository", "5");
            this.purchasedTicketDao = purchasedTicketDao;
        }

        public void performBackgroundTask(PurchasedTicketModel... purchasedTicket) {
            Log("PurchasedTicketRepository", "5");
            executor.execute(() -> {
                if (purchasedTicket.length > 0) {
                    PurchasedTicketModel ticket = purchasedTicket[0];

                    // Insert the purchased ticket
                    purchasedTicketDao.insertPurchasedTicket(ticket);
                    Log("PurchasedTicketRepository", "InsertAsyncTask", "Purchased ticket saved");

                    // Add any additional logic you may need
                }
            });
        }
    }

    private static class UpdateAsyncTask {
        private Executor executor = Executors.newSingleThreadExecutor();
        private PurchasedTicketDao purchasedTicketDao;



        public UpdateAsyncTask(PurchasedTicketDao purchasedTicketDao) {
            Log("PurchasedTicketRepository", "7");
            this.purchasedTicketDao = purchasedTicketDao;
        }

        public void performBackgroundTask(Date redeemedDate, PurchasedTicketModel... purchasedTicket) {
            executor.execute(() -> {
                Log("PurchasedTicketRepository", "8");
                if (purchasedTicket.length > 0) {
                    PurchasedTicketModel ticket = purchasedTicket[0];
                    ticket.setRedeemed(true);
                    ticket.setRedeemedDate(redeemedDate);
                    purchasedTicketDao.updatePurchasedTicket(ticket);
                }
            });
        }
    }
}
