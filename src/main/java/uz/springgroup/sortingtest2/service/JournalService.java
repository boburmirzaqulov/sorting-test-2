package uz.springgroup.sortingtest2.service;

import java.util.List;

public interface JournalService {
    boolean setActiveAll(boolean b, List<Integer> groupIds);
}
