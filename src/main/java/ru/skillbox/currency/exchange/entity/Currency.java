package ru.skillbox.currency.exchange.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.currency.exchange.adapter.ValueAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Valute")
@XmlAccessorType(XmlAccessType.FIELD)
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    @SequenceGenerator(name = "sequence", sequenceName = "create_sequence", allocationSize = 0)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @XmlElement(name = "Name")
    private String name;

    @Column(name = "nominal")
    @XmlElement(name = "Nominal")
    private Long nominal;

    @Column(name = "value")
    @XmlElement(name = "Value")
    @XmlJavaTypeAdapter(ValueAdapter.class)
    private Double value;

    @Column(name = "iso_num_code")
    @XmlElement(name = "NumCode")
    private Long isoNumCode;

    @Column(name = "iso_letter_code")
    @XmlElement(name = "CharCode")
    private String isoLetterCode;

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nominal=" + nominal +
                ", value=" + value +
                ", isoNumCode=" + isoNumCode +
                ", isoLetterCode='" + isoLetterCode + '\'' +
                '}';
    }
}
