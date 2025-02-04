package cn.tealc995.kkmaid.service.api.works;

import cn.tealc995.kikoreu.model.LanguageEdition;
import cn.tealc995.kikoreu.model.MainWorks;
import cn.tealc995.kikoreu.model.Work;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * @program: KK-Maid
 * @description:
 * @author: Leck
 * @create: 2025-02-04 20:04
 */
public class WorksBaseService extends Service<MainWorks> {
    @Override
    protected Task<MainWorks> createTask() {
        return null;
    }

    protected boolean exist(Work work, String name) {
        String id = getId(work.getFullId());
        if (name.contains(id)) {
            return true;
        } else {
            if (work.hasLanguages()) {
                for (LanguageEdition languageEdition : work.getLanguage_editions()) {
                    id = getId(languageEdition.getWorkno());
                    if (name.contains(id)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    protected String getId(String id) {
        String temp;
        if (id.toLowerCase().contains("rj")) {
            temp = id.toLowerCase();
        } else {
            temp = "rj" + id;
        }
        return temp;
    }

}