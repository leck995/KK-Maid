package cn.tealc995.kikoreu.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @program: Asmr-Online
 * @description:
 * @author: Leck
 * @create: 2023-07-19 14:35
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LanguageEdition {
    private String label;
    private String workno;
    private String edition_type;
    private Integer display_order;
    private Integer edition_idedition_id;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getWorkno() {
        return workno;
    }

    public void setWorkno(String workno) {
        this.workno = workno;
    }

    public String getEdition_type() {
        return edition_type;
    }

    public void setEdition_type(String edition_type) {
        this.edition_type = edition_type;
    }

    public Integer getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(Integer display_order) {
        this.display_order = display_order;
    }

    public Integer getEdition_idedition_id() {
        return edition_idedition_id;
    }

    public void setEdition_idedition_id(Integer edition_idedition_id) {
        this.edition_idedition_id = edition_idedition_id;
    }
}