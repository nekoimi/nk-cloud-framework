package com.nekoimi.nk.devtools.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * nekoimi  2022/1/9 21:06
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class SchemaInfo {
    private String name;
}
