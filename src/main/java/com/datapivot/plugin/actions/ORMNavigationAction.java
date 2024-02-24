package com.datapivot.plugin.actions;

import com.datapivot.plugin.context.DataPivotApplication;
import com.datapivot.plugin.i18n.DataPivotBundle;
import com.datapivot.plugin.model.BaseAnAction;
import com.datapivot.plugin.model.DataPivotObject;
import com.datapivot.plugin.model.DataPivotRelation;
import com.datapivot.plugin.tool.MessageUtil;
import com.datapivot.plugin.tool.PsiElementUtil;
import com.intellij.database.view.DatabaseView;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import org.jetbrains.annotations.NotNull;

public class ORMNavigationAction extends BaseAnAction {

    @Override
    protected void action(AnActionEvent e) {
        DataPivotObject dataPivotObject = PsiElementUtil.getDataPivotObject(e.getData(CommonDataKeys.PSI_ELEMENT));
        if (dataPivotObject.getDataPivotMappingSettingInfo() == null) {
            MessageUtil.Hint.error(editor,
                    DataPivotBundle.message("data.pivot.hint.object.mapping.null",
                            dataPivotObject.getPackageReference()));
            return;
        }
        DataPivotRelation dataPivotRelation = DataPivotApplication.ormMapping(dataPivotObject,editor);
        if (dataPivotRelation == null) {
            return;
        }
        DatabaseView.select(dataPivotRelation.getDbColumn(), true);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);
        if (psiElement!=null&&psiElement instanceof PsiField){
            //启用
            e.getPresentation().setEnabledAndVisible(true);
        }else {
            e.getPresentation().setEnabledAndVisible(false);
        }

    }
}
