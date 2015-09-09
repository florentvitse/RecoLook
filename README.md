# RecoLouke
Android App which recognize Logo from base of images

How it works? See a good explanation right here of image processing

  One important thing to understand is that after extracting the keypoints, you only obtain information about their position, and sometimes their coverage area (usually approximated by a circle or ellipse) in the image. While the information about keypoint position might sometimes be useful, it does not say much about the keypoints themselves.
  Depending on the algorithm used to extract keypoint (SIFT, Harris corners, MSER), you will know some general characteristics of the extracted keypoints (e.g. they are centered around blobs, edges, prominent corners...) but you will not know how different or similar one keypoint is to the other.
  Here's two simple examples where only the position and keypoint area will not help us:
  If you have an image A (of a bear on a white background), and another image B, exact copy of A but translated for a few pixels: the extracted keypoints will be the same (on the same part of that bear). Those two images should be recognized as same, or similar.
  But, if the only information we have is their position, and that changed because of the translation, you can not compare the images.
  If you have an image A (let's say, of a duck this time), and another image B, exactly the same duck as in A except twice the size: the extracted keypoints will be the same (same parts of the duck). Those are also same (similar) images.
  But all their sizes (areas) will be different: all the keypoints from the image B will be twice the size of those from image A.

So, here come descriptors: they are the way to compare the keypoints. They summarize, in vector format (of constant length) some characteristics about the keypoints. For example, it could be their intensity in the direction of their most pronounced orientation. It's assigning a numerical description to the area of the image the keypoint refers to.

  Some important things for descriptors are:
    they should be independent of keypoint position
    If the same keypoint is extracted at different positions (e.g. because of translation) the descriptor should be the same.

    they should be robust against image transformations
    Some examples are changes of contrast (e.g. image of the same place during a sunny and cloudy day) and changes of   perspective (image of a building from center-right and center-left, we would still like to recognize it as a same building).

    Of course, no descriptor is completely robust against all transformations (nor against any single one if it is strong, e.g. big change in perspective).

  Different descriptors are designed to be robust against different transformations which is sometimes opposed to the speed it takes to calculate them.

    they should be scale independent
    The descriptors should take scale in to account. If the "prominent" part of the one keypoint is a vertical line of 10px (inside a circular area with radius of 8px), and the prominent part of another a vertical line of 5px (inside a circular area with radius of 4px) -- these keypoints should be assigned similar descriptors.

  Now, that you calculated descriptors for all the keypoinst, you have a way to compare those keypoints. For a simple example of image matching (when you know the images are of the same object, and would like to identify the parts in different images that depict the same part of the scene, or would like to identify the perspective change between two images), you would compare every keypoint descriptor of one image to every keypoint descriptor of the other image. As the descriptors are vectors of numbers, you can compare them with something as simple as Euclidian distance. There are some more complex distances that can be used as a similarity measure, of course. But, in the end, you would say that the keypoints whose descriptors have the smallest distance between them are matches, e.g. same "places" or "parts of objects" in different images.
