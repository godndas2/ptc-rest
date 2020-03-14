package com.example.model.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement // Class 에 사용하는 annotation 으로 해당 클래스가 XML 특정 노드의 루트
public class Vets {

    private List<Vet> vets;

    @XmlElement
    public List<Vet> getVets() {
        if (vets == null) {
            vets = new ArrayList<>();
        }
        return vets;
    }
}
