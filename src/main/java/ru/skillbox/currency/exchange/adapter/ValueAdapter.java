package ru.skillbox.currency.exchange.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ValueAdapter extends XmlAdapter<String, Double> {
    @Override
    public Double unmarshal(String v) {
        return Double.parseDouble(v.replace(",", "."));
    }

    @Override
    public String marshal(Double v) {
        return v.toString();
    }
}
