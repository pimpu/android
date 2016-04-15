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
import info.alchemistdigital.e_carrier.model.Enquired_Details_Item;
import info.alchemistdigital.e_carrier.utilities.Queries;

/**
 * Created by user on 1/16/2016.
 */
public class Enquired_Details_Adapter extends RecyclerView.Adapter<Enquired_Details_Adapter.EnquiryViewHolder> {

    List<Enquired_Details_Item> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    View views;

    public Enquired_Details_Adapter(Context context, List<Enquired_Details_Item> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public EnquiryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.enquired_row_lauout, parent, false);
        EnquiryViewHolder holder = new EnquiryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(EnquiryViewHolder holder, int position) {

        Enquired_Details_Item current = data.get(position);
        holder.destinationTitle.setText(current.getBeginning() + " on " + current.getEnquiryDate());
//        holder.driverIdTitle.setText("Driver Detail: " + current.getDriverData());

        holder.startService.setVisibility(views.GONE);

        Cursor bookedEnquiryCursor= Queries.db.rawQuery("SELECT * FROM booked_enquiry WHERE enquiryId="+current.getEnquiryId()+";", null);
        if(bookedEnquiryCursor!=null && bookedEnquiryCursor.getCount()>0){
            bookedEnquiryCursor.moveToFirst();
            do {
                if( !bookedEnquiryCursor.getString( bookedEnquiryCursor.getColumnIndex("driverData")).equals("not assign any driver till.") ){
                    holder.startService.setVisibility(views.VISIBLE);
                }
            } while (bookedEnquiryCursor.moveToNext());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class EnquiryViewHolder extends RecyclerView.ViewHolder {
        TextView destinationTitle, driverIdTitle;
        ImageView startService;

        public EnquiryViewHolder(View itemView) {
            super(itemView);
            views = itemView;

            destinationTitle = (TextView) itemView.findViewById(R.id.desinationTitle);
//            driverIdTitle = (TextView) itemView.findViewById(R.id.driverIdTitle);
            startService = (ImageView) itemView.findViewById(R.id.idEnquiryStarted);

        }
    }
}
