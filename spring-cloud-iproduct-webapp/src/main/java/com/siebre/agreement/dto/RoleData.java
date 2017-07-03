package com.siebre.agreement.dto;

import com.siebre.agreement.dto.annotation.Kind;

import java.util.Map;


public class RoleData<K, V> extends ComponentData<K, V> {

	public RoleData(Map<K, V> map) {
		super(map);
	}

	public RoleData() {
		super();
	}

	@Kind
	public String getKind() {
		return (String) this.get("kind");
	}

	public void setKind(String kind) {
		this.put((K) "kind", (V) kind);
	}
}
