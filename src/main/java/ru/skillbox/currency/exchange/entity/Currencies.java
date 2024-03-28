package ru.skillbox.currency.exchange.entity;

import lombok.Getter;

import javax.xml.bind.annotation.*;
import java.util.List;
@Getter
@XmlRootElement(name = "ValCurs")
@XmlAccessorType(XmlAccessType.FIELD)
public class Currencies {
    @XmlElement(name = "Valute")
    private List<Currency> currencies;
}

