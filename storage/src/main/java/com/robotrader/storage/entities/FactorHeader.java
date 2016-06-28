package com.robotrader.storage.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema="robotrader", name="FACTOR")
public class FactorHeader implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="CODE")
	private String code;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="IS_PAPER")
	private Boolean paper;
        
        @Column(name = "SCALE")
        private Long scale;
	
	public FactorHeader() {		
	}
	
	public FactorHeader(String code, String name, Boolean paper, Long scale) {
		super();
		this.code = code;
		this.name = name;
		this.paper = paper;
                this.scale = scale;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getPaper() {
		return paper;
	}

	public void setPaper(Boolean paper) {
		this.paper = paper;
	}

        public Long getScale() {
            return scale;
        }

        public void setScale(Long scale) {
            this.scale = scale;
        }

	@Override
	public String toString() {
		return "Factor [id=" + id + ", code=" + code + ", name=" + name
				+ ", paper=" + paper + "]";
	}
}
