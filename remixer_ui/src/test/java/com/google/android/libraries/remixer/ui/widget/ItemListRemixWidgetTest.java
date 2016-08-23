/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.libraries.remixer.ui.widget;

import android.view.LayoutInflater;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.libraries.remixer.ItemListRemix;
import com.google.android.libraries.remixer.RemixCallback;
import com.google.android.libraries.remixer.ui.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(
    sdk = 21,
    manifest = "src/main/AndroidManifest.xml",
    packageName = "com.google.android.libraries.remixer.ui")
public class ItemListRemixWidgetTest {
  private static final String TITLE = "Color";
  private static final String KEY = "color";
  private static final String[] ITEM_LIST = new String[]{
      "red",
      "blue",
      "yellow",
      "green"
  };
  private static final int DEFAULT_VALUE_INDEX = 2;

  @Mock
  RemixCallback<String> mockCallback;

  private ItemListRemix<String> remix;
  private ItemListRemixWidget view;
  private TextView name;
  private Spinner spinner;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    remix = new ItemListRemix<String>(
        TITLE,
        KEY,
        ITEM_LIST[DEFAULT_VALUE_INDEX],
        Arrays.asList(ITEM_LIST),
        mockCallback,
        R.layout.item_list_remix_widget);
    view = (ItemListRemixWidget) LayoutInflater.from(RuntimeEnvironment.application)
        .inflate(R.layout.item_list_remix_widget, null);
    view.bindRemix(remix);
    spinner = (Spinner) view.findViewById(R.id.itemListRemixSpinner);
    name = (TextView) view.findViewById(R.id.itemListRemixName);
  }

  @Test
  public void defaultIsShown() {
    assertEquals(TITLE, name.getText());
    assertEquals(ITEM_LIST[DEFAULT_VALUE_INDEX], spinner.getSelectedItem());
  }

  @Test
  public void callbackIsCalled() {
    // Check that the callback  was called. This should've happened during setUp()
    verify(mockCallback, times(1)).onValueSet(remix);
    spinner.setSelection(0);
    // Check the selected item was changed on the UI
    assertEquals(ITEM_LIST[0], spinner.getSelectedItem());
    // After changing the selection, check that the callback was called once again
    verify(mockCallback, times(2)).onValueSet(remix);
    spinner.setSelection(1);
    // Check the selected item was changed on the UI
    assertEquals(ITEM_LIST[1], spinner.getSelectedItem());
    // After changing the selection, check that the callback was called once again
    verify(mockCallback, times(3)).onValueSet(remix);
  }
}
