#include <cstdio>
#include <cstring>
#include <string>
#include <fstream>
#include <set>
#include <map>


#include <opencv2/opencv.hpp>
#include "opencv2/core/core.hpp"
#include "opencv2/features2d/features2d.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/calib3d/calib3d.hpp"
#include "opencv2/nonfree/nonfree.hpp"

using namespace cv;

const int DICTIONARY_SIZE = 200;
std::vector<std::string> classes_names;
std::map<std::string, Mat> classes_training_data;


void testClassifiers(string testImageListFileName, string vocabularyFileName) {
	//prepare BOW descriptor extractor from the vocabulary already computed  
	Mat vocabulary;
	FileStorage fs(vocabularyFileName.c_str(), FileStorage::READ);
	fs["vocabulary"] >> vocabulary;
	fs.release();

	//create SIFT feature point extracter
	cv::Ptr<cv::FeatureDetector> detector;
	detector = cv::Ptr<cv::FeatureDetector>(new cv::SIFT());

	//create SIFT descriptor extractor
	cv::Ptr<cv::DescriptorExtractor> extractor;
	extractor = cv::Ptr<cv::DescriptorExtractor>(new cv::SIFT());

	//create a matcher with FlannBased Euclidien distance (possible also with BruteForce-Hamming)
	Ptr<DescriptorMatcher> matcher = DescriptorMatcher::create("FlannBased");

	//create BoF (or BoW) descriptor extractor
	BOWImgDescriptorExtractor bowide(extractor, matcher);

	//Set the dictionary with the vocabulary we created in the first step
	bowide.setVocabulary(vocabulary);


	/*const int classNumber = 8;
		std::string class_names[classNumber] = { "shabyt", "vokzal", "hanshatyr", "triumf",
		"baiterek", "pyramid", "defence", "nu" };*/
		const int classNumber = 3;
		std::string class_names[classNumber] = { "Coca", "Pepsi", "Sprite" };

	CvSVM classifiers[classNumber];
	for (int i = 0; i < classNumber; i++) {
		//open the file to write the resultant descriptor
		classifiers[i].load(("classifiers/" + class_names[i] + ".xml").c_str());
		std::cout << "classifiers/" + class_names[i] + ".xml loaded." << std::endl;
	}
	string testImageName;
	Mat input, response_hist, descriptor;
	std::vector<KeyPoint> keypoints;

	std::fstream testImageListFile;
	testImageListFile.open(testImageListFileName, std::fstream::in);

	int totalTestCases = 0, correct = 0;

	while (testImageListFile >> testImageName) {

		//test images with the location. 
		input = imread((testImageName).c_str(), CV_LOAD_IMAGE_GRAYSCALE);

		//   std::cout << "Loaded " + testImageName << input.rows << " x " << input.cols << std::endl;
		totalTestCases++;
		// parser the file name 
		int index = testImageName.find('_');
		int index2 = testImageName.find('/');
		string actualClassName = testImageName.substr(index2 + 1, index - (index2 + 1));

		//Detect SIFT keypoints (or feature points)
		detector->detect(input, keypoints);
		//extract BoW (or BoF) descriptor from given image
		bowide.compute(input, keypoints, response_hist);
		//std::cout << "Response hist " << response_hist.cols << std::endl;


		float minf = FLT_MAX;
		std::string bestMatch;
		// loop for all classes
		for (int i = 0; i < classNumber; i++) {
			// classifier prediction based on reconstructed histogram
			float res = classifiers[i].predict(response_hist, true);
			if (res < minf) {
				minf = res;
				bestMatch = class_names[i];
			}
		}
		std::cout << testImageName << " is " << bestMatch << std::endl;
		if (actualClassName == bestMatch)
			correct++;
		else {
			std::cout << actualClassName << " != " << bestMatch << std::endl;
		}
	}
	testImageListFile.close();

	std::cout << "Total: " << totalTestCases << ", correct: " << correct << "\n";
	std::cout << "Accuracy: " << 1.0 * correct / totalTestCases << "\n";
}

int main(int argc, char* argv[]) {

	
	if (argc != 3) {
		std::cout << "Usage: testBOW <files for testing> <vocabulary file>\n";

		std::cout << "For exemple testBOW.exe test_images.txt vocabulary.yml;\n"
			<< "\n";

		return 0;
	}
	
	/*
	 argv[1] = "test_images.txt";
	 argv[2] = "vocabulary.yml";
	 */
	 
	testClassifiers(argv[1], argv[2]);
	





	return 0;
}








