package com.ness.zkworkshop.web.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.zul.DefaultStepModel;
import org.zkoss.zkmax.zul.StepModel;
import org.zkoss.zkmax.zul.Stepbar;
import org.zkoss.zul.Label;

public class StepbarComposer  extends SelectorComposer {

    @Wire("#stepbar")
    private Stepbar stepbar;

    @Wire("#stepContent")
    private Component stepContent;

    private StepModel<UiStepInfo> model;
    private final UiStepInfo additionalStep = new UiStepInfo("Step 2.1");

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        model = new DefaultStepModel();
        model.add(new UiStepInfo("Step 1"));
        model.add(new UiStepInfo("Step 2"));
        model.add(new UiStepInfo("Step 3"));
        stepbar.setModel(model);
        updateStepContent();
    }

    @Listen("onClick=#next")
    public void next() {
        model.next();
        updateStepContent();
    }

    @Listen("onClick=#back")
    public void back() {
        model.back();
        updateStepContent();
    }

    private void updateStepContent() {
        stepContent.getChildren().clear();
        stepContent.appendChild(new Label("Dynamic content for: '" + model.getActiveStep().getTitle() + "'"));
    }

    @Listen("onClick=#toggleAdditionalStep")
    public void toggleAdditionalStep() {
        if(additionalStep.equals(model.getSteps().getElementAt(2))) {
            if(additionalStep.equals(model.getActiveStep())) {
                model.next();
            }
            model.remove(additionalStep);
        } else {
            model.add(2, additionalStep);
        }
    }

    @Listen("onClick=#toggleStepComplete")
    public void toggleStepComplete() {
        stepbar.getActiveStep().setComplete(!stepbar.getActiveStep().isComplete());
    }

    @Listen("onClick=#toggleStepError")
    public void toggleStepError() {
        stepbar.getActiveStep().setError(!stepbar.getActiveStep().isError());
    }

    public static class UiStepInfo {
        private String title;

        public UiStepInfo(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}
