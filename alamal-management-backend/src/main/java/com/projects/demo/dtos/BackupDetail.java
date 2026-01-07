package com.projects.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class BackupDetail {
    File backupDir;
    LocalDate createdDate;
}
