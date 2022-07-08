package uz.springgroup.sortingtest2.service;

import uz.springgroup.sortingtest2.entity.Journal;

import java.util.List;

public interface SubjectService {
    boolean setActiveAll(boolean b, List<Journal> journals);
}
