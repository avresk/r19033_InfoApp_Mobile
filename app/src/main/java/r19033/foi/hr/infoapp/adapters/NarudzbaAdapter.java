package r19033.foi.hr.infoapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import r19033.foi.hr.infoapp.R;
import r19033.foi.hr.infoapp.models.Narudzba;
import r19033.foi.hr.infoapp.utils.SortLocation;

public class NarudzbaAdapter extends ArrayAdapter<Narudzba> {

    Context context;
    private static LayoutInflater inflater = null;
    private ArrayList<Narudzba> itemList;

    public NarudzbaAdapter(Context context, ArrayList<Narudzba> list) {
        super(context, R.layout.narudzba_view_model, list);
        this.context = context;

        try {
            itemList = list;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return itemList.size();
    }


    public Narudzba getItem(Narudzba position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        TextView displayID;
        TextView displayName;
        TextView displayLocation;
        TextView displayDate;
        TextView displayPaymentType;
        TextView displayComment;
        ImageView displayStatus;

    }

    public void destroyItem(int position) {
        itemList.remove(position);
        notifyDataSetChanged();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.narudzba_view_model, null);
                holder = new ViewHolder();

                holder.displayID = (TextView) vi.findViewById(R.id.tvNarudzbaID);
                holder.displayName = (TextView) vi.findViewById(R.id.tvKorisnik);
                holder.displayDate = (TextView) vi.findViewById(R.id.tvDatumKreiranja);
                holder.displayPaymentType = (TextView) vi.findViewById(R.id.tvNacinPlacanja);
                holder.displayComment = (TextView) vi.findViewById(R.id.tvNapomena);
                holder.displayLocation = (TextView) vi.findViewById(R.id.tvLokacijaKorisnika);
                holder.displayStatus = (ImageView) vi.findViewById(R.id.ivStatusNarudzbe);

                vi.setTag(holder);

            } else {

                holder = (ViewHolder) vi.getTag();
            }

            Narudzba narudzba = itemList.get(position);

            holder.displayID.setText(narudzba.getId().toString());
            holder.displayName.setText(narudzba.getKorisnikIme() + " " + narudzba.getKorisnikPrezime());
            String[] loc;
            loc = SortLocation.getInstance().returnLocation(narudzba.getLokacija());
            holder.displayLocation.setText(narudzba.getLokacija() + "; " + loc[1] + "; " + loc[0]);
            holder.displayDate.setText(narudzba.getDatum_kreiranja());
            holder.displayComment.setText(narudzba.getNapomena());
            holder.displayPaymentType.setText(narudzba.getNacin_placanja());

            if (narudzba.isIzvrsena()) {
                holder.displayStatus.setImageResource(R.drawable.status_done);
            } else {
                holder.displayStatus.setImageResource(R.drawable.status_not_done);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return vi;

    }
}
