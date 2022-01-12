package com.nekoimi.nk.devtools.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * nekoimi  2022/1/9 21:07
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TableInfo {
    private String name;
    private String comment;
}
