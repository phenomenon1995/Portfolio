Initially thought that keras wasn't importing because VSCode wasn't showup up in VSCode auto complete so I copied the lecture code snippet for creating a sequential keras model and used default layer setting from lecture source code. To verify that keras models were successfully being created on my machine. 

Tried reading doc for keras functional API. It wasconfusing whether or not a single image should be treated as a single input or as multiple, so i proceded with removing the lecture code and starting fresh, following along with TensorFlow's Functional API tutorial. I added a single Conv2D, MaxPool, and Hidden Dense layer. each with 30 units and reLU activation. Accuracy very poor. less than 10%, but, after seeing that I only had 1 unit in the hidden Dense layer, changed to 30 and changed to sigmoid activation. Added additional 2dConv layer. Changed output layer to softmax activation. accuracy skyrocketed. 95%

From there I just kept tinkering with the settings, I did the following:

    Added dropout layer to mitigate overfitting, accuracy unchanged. 95%

    Reduced MaxPool2D to 2x2 grid and placed another after the second 2DConv layer. Minor Improvement. 96%

    Removed the 2nd max pool and changed unit sized for 2DConv and Dense layer to 60 units. No improvement. 96%

    Decreased size of 2dConv Filter to 2x2, Accuracy 97.5%

    Added a AveragePooling2D layer after second pass of 2DConv filter. Reduced training time and improved accuracy. 98%. I really wanted to try to get at least 99% accuracy, but this seemed like the highest that I could do.
