package com.Swayam.gstbill.gstbill;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

//import com.Swayam.gstbill.R;
import com.Swayam.gstbill.gstbill.data.GSTBillingContract.GSTBillingCustomerEntry;
import com.Swayam.gstbill.gstbill.data.GSTBillingContract.GSTBillingEntry;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillHolder> {

    final private BillItemClickListener clickListener;
    private Context mContext;
    private Cursor mCursor;
    private int dividerColor;

    public BillAdapter(Context mContext, BillItemClickListener clickListener, int dividerColor) {
        this.mContext = mContext;
        this.clickListener = clickListener;
        this.dividerColor = dividerColor;
    }

    @Override
    public BillHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_item_bills_layout, parent, false);
        return new BillHolder(view);
    }

    @Override
    public void onBindViewHolder(BillHolder holder, int position) {
        if (mCursor.moveToPosition(position)) {

            long id = mCursor.getLong(mCursor.getColumnIndex(GSTBillingEntry._ID));
            String customerName = mCursor.getString(mCursor.getColumnIndex(GSTBillingEntry.PRIMARY_COLUMN_NAME));
            String phoneNumber = mCursor.getString(mCursor.getColumnIndex(GSTBillingEntry.PRIMARY_COLUMN_PHONE_NUMBER));
            String date = mCursor.getString(mCursor.getColumnIndex(GSTBillingEntry.PRIMARY_COLUMN_DATE));

            int totalAmount = getTotalAmount(id);

            holder.customerNameTv.setText(customerName);
            if (phoneNumber.length() == 10) {
                holder.phoneNumberTv.setText(phoneNumber);
            } else {
                holder.phoneNumberTv.setText("");
            }
            holder.totalAmountTv.setText(mContext.getString(R.string.inr) + String.valueOf(totalAmount));
            holder.dateTv.setText(date);

            holder.itemView.setTag(R.id.bill_detail_id, id);
            holder.itemView.setTag(R.id.bill_detail_customer_name, customerName);
            holder.itemView.setTag(R.id.bill_detail_phone_number, phoneNumber);

            holder.divider.setBackgroundColor(dividerColor);

        }
    }

    private int getTotalAmount(long id) {
        Cursor amountCursor = mContext.getContentResolver().query(
                GSTBillingEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build(),
                new String[]{GSTBillingCustomerEntry.SECONDARY_COLUMN_FINAL_PRICE, GSTBillingCustomerEntry.SECONDARY_COLUMN_QUANTITY},
                null,
                null,
                null
        );

        int totalAmount = 0;
        int items = amountCursor.getCount();
        for (int i = 0; i < items; i++) {
            if (amountCursor.moveToPosition(i)) {
                int finalPrice = (int) amountCursor.getFloat(0);
                int qty = amountCursor.getInt(1);
                totalAmount += (finalPrice * qty);
            }
        }
        amountCursor.close();
        return totalAmount;
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        } else {
            return mCursor.getCount();
        }
    }

    void swapCursor(Cursor newCursor, int newDividerColor) {
        mCursor = newCursor;
        dividerColor = newDividerColor;
        notifyDataSetChanged();
    }

    public interface BillItemClickListener {
        void onBillItemClick(String clickedBillId, String customerName, String phoneNumber);
    }

    public class BillHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView customerNameTv, phoneNumberTv, totalAmountTv, dateTv;
        View divider;

        public BillHolder(View itemView) {
            super(itemView);

            customerNameTv = (TextView) itemView.findViewById(R.id.bill_customer_name);
            phoneNumberTv = (TextView) itemView.findViewById(R.id.bill_phone_number);
            totalAmountTv = (TextView) itemView.findViewById(R.id.bill_total_amount);
            dateTv = (TextView) itemView.findViewById(R.id.bill_date);
            divider = itemView.findViewById(R.id.bill_divider);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListener.onBillItemClick(
                    String.valueOf(itemView.getTag(R.id.bill_detail_id)),
                    (String) itemView.getTag(R.id.bill_detail_customer_name),
                    (String) itemView.getTag(R.id.bill_detail_phone_number)
            );
        }
    }
}
