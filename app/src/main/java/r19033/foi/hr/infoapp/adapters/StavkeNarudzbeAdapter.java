package r19033.foi.hr.infoapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import r19033.foi.hr.infoapp.R;
import r19033.foi.hr.infoapp.models.StavkaNarudzbe;

public class StavkeNarudzbeAdapter extends ArrayAdapter<StavkaNarudzbe> {

  Context context;
  private static LayoutInflater inflater = null;
  private ArrayList<StavkaNarudzbe> itemList;

  public StavkeNarudzbeAdapter(Context context, ArrayList<StavkaNarudzbe> list) {
    super(context, R.layout.stavka_narudzbe_view_model, list);
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


  public StavkaNarudzbe getItem(StavkaNarudzbe position) {
    return position;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  public static class ViewHolder {
    TextView displayName;
    TextView displayQuantity;
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
        vi = inflater.inflate(R.layout.stavka_narudzbe_view_model, null);
        holder = new ViewHolder();

        holder.displayName = (TextView) vi.findViewById(R.id.tvStavkaNaziv);
        holder.displayQuantity = (TextView) vi.findViewById(R.id.tvStavkaKolicina);

        vi.setTag(holder);

      } else {

        holder = (ViewHolder) vi.getTag();
      }

      StavkaNarudzbe stavka = itemList.get(position);


      holder.displayName.setText(stavka.getNaziv());
      holder.displayQuantity.setText(String.valueOf(stavka.getKolicina()));

    } catch (Exception e) {
      e.printStackTrace();

    }
    return vi;

  }
}

