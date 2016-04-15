package info.alchemistdigital.e_carrier.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import info.alchemistdigital.e_carrier.R;
import info.alchemistdigital.e_carrier.model.Book_Service_Item;
import info.alchemistdigital.e_carrier.utilities.Queries;

/**
 * Created by user on 12/24/2015.
 */
public class Book_Service_Adapter extends RecyclerView.Adapter<Book_Service_Adapter.BookViewHolder> {

    List<Book_Service_Item> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private View views;

    public Book_Service_Adapter(Context context, List<Book_Service_Item> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void swapItems(List< Book_Service_Item > todolist){
        this.data = todolist;
        notifyDataSetChanged();
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.book_service_row_layout, parent, false);

        BookViewHolder holder = new BookViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book_Service_Item current = data.get(position);
        holder.fromTitle.setText("From: " + current.getFromArea());
//        holder.toTitle.setText("To: " + current.getToArea());
        holder.dateTitle.setText("Timing: "+current.getDate());

        holder.startService.setVisibility(views.GONE);
        holder.stopService.setVisibility(views.GONE);

        Cursor bookedEnquiryCursor= Queries.db.rawQuery("SELECT * FROM booked_enquiry WHERE enquiryId="+current.getBookingId()+";", null);
        if(bookedEnquiryCursor!=null && bookedEnquiryCursor.getCount()>0){

            bookedEnquiryCursor.moveToFirst();
            do {
                if(bookedEnquiryCursor!=null && bookedEnquiryCursor.getCount() > 0){
                    int enquiryId = bookedEnquiryCursor.getInt(bookedEnquiryCursor.getColumnIndex("enquiryId"));

                    Cursor serviceStatusCursor= Queries.db.rawQuery("SELECT * FROM enquiry_start_service_status WHERE enquiryId="+enquiryId+";", null);
                    serviceStatusCursor.moveToFirst();
                    do {

                        if(serviceStatusCursor!=null && serviceStatusCursor.getCount() > 0){
                            holder.startService.setVisibility(views.VISIBLE);
                        } else {
                            holder.stopService.setVisibility(views.VISIBLE);
                        }

                    } while (serviceStatusCursor.moveToNext());

                }
            } while (bookedEnquiryCursor.moveToNext());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        TextView fromTitle,toTitle,dateTitle;
        ImageView startService,stopService;

        public BookViewHolder(View itemView) {
            super(itemView);

            views = itemView;

            fromTitle = (TextView) itemView.findViewById(R.id.fromAreaTitle);
//            toTitle = (TextView) itemView.findViewById(R.id.toAreaTitle);
            dateTitle = (TextView) itemView.findViewById(R.id.dateTitle);

            startService = (ImageView) itemView.findViewById(R.id.idServiceStarted);
            stopService  = (ImageView) itemView.findViewById(R.id.idServiceStoped);

        }
    }
}
