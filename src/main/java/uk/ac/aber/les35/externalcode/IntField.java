package uk.ac.aber.les35.externalcode;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * This IntField class is taken from a Stackoverflow answer by user jewelsea
 * Found here: https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx/18959399
 *
 * The class creates a textfield that is limited to a specific range of integers, useful in allowing users to type a
 * specific value for a parameter.
 */
public class IntField extends TextField {
    final private IntegerProperty value;
    final private int minValue;
    final private int maxValue;

    // expose an integer value property for the text field.
    public int  getValue()                 { return value.getValue(); }
    public void setValue(int newValue)     { value.setValue(newValue); }
    public IntegerProperty valueProperty() { return value; }

    private boolean bitwise = false;

    public IntField(int minValue, int maxValue, int initialValue) {
        if (minValue > maxValue)
            throw new IllegalArgumentException(
                    "IntField min value " + minValue + " greater than max value " + maxValue
            );
        if (maxValue < minValue)
            throw new IllegalArgumentException(
                    "IntField max value " + minValue + " less than min value " + maxValue
            );
        if (!((minValue <= initialValue) && (initialValue <= maxValue)))
            throw new IllegalArgumentException(
                    "IntField initialValue " + initialValue + " not between " + minValue + " and " + maxValue
            );

        // initialize the field values.
        this.minValue = minValue;
        this.maxValue = maxValue;
        value = new SimpleIntegerProperty(initialValue);
        setText(initialValue + "");

        final IntField intField = this;

        // make sure the value property is clamped to the required range
        // and update the field's text to be in sync with the value.
        value.addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (newValue == null) {
                    intField.setText("");
                } else {
                    if (newValue.intValue() < intField.minValue) {
                        value.setValue(intField.minValue);
                        return;
                    }

                    if (newValue.intValue() > intField.maxValue) {
                        value.setValue(intField.maxValue);
                        return;
                    }

                    if (newValue.intValue() == 0 && (textProperty().get() == null || "".equals(textProperty().get()))) {
                        // no action required, text property is already blank, we don't need to set it to 0.
                    } else {
                        intField.setText(newValue.toString());
                    }
                }
            }
        });

        // restrict key input to numerals.
        this.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override public void handle(KeyEvent keyEvent) {
                if(intField.minValue<0) {
                    if (!"-0123456789".contains(keyEvent.getCharacter())) {
                        keyEvent.consume();
                    }
                }
                else {
                    if (!"0123456789".contains(keyEvent.getCharacter())) {
                        keyEvent.consume();
                    }
                }
            }
        });

        // ensure any entered values lie inside the required range.
        this.textProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue == null || "".equals(newValue) || (intField.minValue<0 && "-".equals(newValue))) {
                    value.setValue(0);
                    return;
                }

                final int intValue = Integer.parseInt(newValue);

                if (intField.minValue > intValue || intValue > intField.maxValue) {
                    textProperty().setValue(oldValue);
                }

                value.set(Integer.parseInt(textProperty().get()));
            }
        });
    }

    public boolean isBitwise() {
        return bitwise;
    }

    public void setBitwise(boolean bitwise) {
        this.bitwise = bitwise;
    }
}