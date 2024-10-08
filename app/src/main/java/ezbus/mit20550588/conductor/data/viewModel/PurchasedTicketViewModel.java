package ezbus.mit20550588.conductor.data.viewModel;

import static ezbus.mit20550588.conductor.util.Constants.Log;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import ezbus.mit20550588.conductor.data.model.PurchasedTicketModel;
import ezbus.mit20550588.conductor.data.repository.PurchasedTicketRepository;

public class PurchasedTicketViewModel extends AndroidViewModel {

    private PurchasedTicketRepository repository;
    private LiveData<List<PurchasedTicketModel>> allPurchasedTickets;

    public PurchasedTicketViewModel(@NonNull Application application) {
        super(application);
        repository = new PurchasedTicketRepository(application);
        allPurchasedTickets = repository.getAllPurchasedTickets();

        Log("PurchasedTicketViewModel", "1");
    }

    public void insert(PurchasedTicketModel purchasedTicket) {
        Log("PurchasedTicketViewModel", "2");
        repository.insert(purchasedTicket);

    }

    public  LiveData<Integer> getCountOfNewTickets(){
        return repository.getCountOfNewTickets();
    }

    public LiveData<List<PurchasedTicketModel>> getPurchasedTickets() {
        Log("PurchasedTicketViewModel", "3");
        return allPurchasedTickets;
    }


    // Update ticket when the server return it as redeemed
    public void update(PurchasedTicketModel purchasedTicket, Date redeemedDate) {
        repository.updatePurchasedTicket(purchasedTicket, redeemedDate);
    }
}
