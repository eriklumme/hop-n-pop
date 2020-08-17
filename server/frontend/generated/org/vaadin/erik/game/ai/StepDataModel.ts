import StepData from './StepData';

// @ts-ignore
import {ObjectModel,StringModel,NumberModel,ArrayModel,BooleanModel,Required} from '@vaadin/form';

// @ts-ignore
import {Email,Null,NotNull,NotEmpty,NotBlank,AssertTrue,AssertFalse,Negative,NegativeOrZero,Positive,PositiveOrZero,Size,Past,PastOrPresent,Future,FutureOrPresent,Digits,Min,Max,Pattern,DecimalMin,DecimalMax} from '@vaadin/form';

/**
 * This module is generated from org.vaadin.erik.game.ai.StepData.
 * All changes to this file are overridden. Please consider to make changes in the corresponding Java file if necessary.
 */
export default class StepDataModel<T extends StepData = StepData> extends ObjectModel<T> { 
  static createEmptyValue: () => StepData;
  public readonly targetIndex = new NumberModel(this, 'targetIndex');
}
