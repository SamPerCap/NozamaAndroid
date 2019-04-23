package com.example.nozamaandroid.Models;

import com.example.nozamaandroid.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ProductsMockData {
    ArrayList<BEProducts> prods;

    public ProductsMockData()
    {
        prods = new ArrayList<BEProducts>();
        prods.add(new BEProducts("JacobCandy", "Sweet, delicious", 250.5, 5, 55.620532, 8.481840, R.drawable.cake));
        prods.add(new BEProducts("Samuel Cake", "Juicy fruit",25, 5, 55.620532, 8.479160, R.drawable.cake));
        prods.add(new BEProducts("Kriss Chocklate", "Brown and soft", 69, 5, 55.620532, 8.479160, R.drawable.cake));
    }

    public ArrayList<BEProducts> getAllProducts()
    { return prods; }

    public String[] getProducts()
    {
        String[] res = new String[prods.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = prods.get(i).getP_name();
        }
        return res;
    }
}
