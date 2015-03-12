package com.yhlearningclient.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yhlearningclient.R;
import com.example.yhlearningclient.R.color;
import com.yhlearningclient.factory.TestPaperFactory;
import com.yhlearningclient.factory.UTestPaperFactory;
import com.yhlearningclient.model.QuestionTypeEnum;
import com.yhlearningclient.model.TestPaperQuestion;
import com.yhlearningclient.utils.ImageTools;

@SuppressLint({ "ResourceAsColor", "ResourceAsColor", "ResourceAsColor", "ResourceAsColor" })
public class QuestionSingleStat extends Activity {
	
	private TestPaperFactory testPaperFactory = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_single);
		int subjectLocation = getIntent().getIntExtra("questionId", 0);
		try {
			updateSingle(subjectLocation);
		} catch (ParseException e) {
			Toast.makeText(this,getResources().getString(R.string.parseError),Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 更新
	 * @throws ParseException 
	 */
	@SuppressLint("ResourceAsColor")
	private void updateSingle(int location) throws ParseException{
		
		String condition = " question_id=" +location;
		TestPaperQuestion testPaperQuestion = null;
		List<TestPaperQuestion> TqpList = testPaperFactory.createTestPaper(QuestionSingleStat.this).getQuestionById(condition);
		
		int totalUser = UTestPaperFactory.createUTestPaper(QuestionSingleStat.this).getByTotalCount(String.valueOf(location));
		int rightUser = UTestPaperFactory.createUTestPaper(QuestionSingleStat.this).getByRCount(String.valueOf(location));
		
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(2);
		String rightResult = numberFormat.format((float)rightUser/(float)totalUser*100);
		
		if(TqpList.size() > 0){
			testPaperQuestion = TqpList.get(0);
		}
		
		final TextView singleIndexView = (TextView)findViewById(R.id.singleIndex);
		final ImageView singleContentView = (ImageView)findViewById(R.id.singleContent);
		final LinearLayout singleAnswersLayout = (LinearLayout)findViewById(R.id.singleAnswerLayout);
		final TextView singleAnswerAnalysis = (TextView)findViewById(R.id.singleAnswerAnalysis);
		
		singleIndexView.setText(String.format(getResources().getString(R.string.subjectIndex), testPaperQuestion.getName()));
		singleAnswerAnalysis.setText("总人数："+totalUser+"  正确率：" + rightResult + "%");
		
		byte[] result = testPaperQuestion.getNote();
		Bitmap bitmap = ImageTools.getBitmapFromByte(result);
		singleContentView.setImageBitmap(bitmap);
		
		RadioGroup rg = new RadioGroup(this);
		String[] str = {"A","B","C","D"};
		for(int i=0; i<str.length; i++){
			final RadioButton rb = new RadioButton(this);
			rb.setButtonDrawable(R.drawable.true_uncheck);
			rb.setText(str[i]);
			rb.setPadding(5, 0, 40, 0);
			rb.setTextSize(18);
			rb.setClickable(false);
			if(testPaperQuestion.getQuestionType() == QuestionTypeEnum.SingleSelect.toValue()){
				if(testPaperQuestion.getAnswer().equals(str[i])){
					rb.setTextColor(R.color.correctAnswerColor1);
					rb.setButtonDrawable(R.drawable.true_check);
				}
				rg.setOrientation(LinearLayout.HORIZONTAL);
				rg.addView(rb);
			} else if(testPaperQuestion.getQuestionType() == QuestionTypeEnum.MultiSelect.toValue()){
				char[] cAnswer = testPaperQuestion.getAnswer().toCharArray();
				for(int j =0; j < cAnswer.length; j++){
					String uAnswer = String.valueOf(cAnswer[j]);
					
					if(str[i].equals(uAnswer)){
						rb.setTextColor(R.color.correctAnswerColor1);
						rb.setButtonDrawable(R.drawable.true_check);
					}
				}
				
				rg.setOrientation(LinearLayout.HORIZONTAL);
				rg.addView(rb);
			}
		}
		if(testPaperQuestion.getQuestionType() == QuestionTypeEnum.Judge.toValue()){
				final RadioButton rb = new RadioButton(this);
				rb.setButtonDrawable(R.drawable.true_uncheck);
				rb.setPadding(5, 0, 40, 0);
				rb.setTextSize(18);
				rb.setClickable(false);
				String uAnswer = testPaperQuestion.getAnswer();
				if("0".equals(uAnswer)){
					rb.setText("错");
					rb.setTextColor(R.color.correctAnswerColor1);
					rb.setButtonDrawable(R.drawable.true_check);
				} else if("1".equals(uAnswer)){
					rb.setText("对");
					rb.setTextColor(R.color.correctAnswerColor1);
					rb.setButtonDrawable(R.drawable.true_check);
				}
				rg.setOrientation(LinearLayout.HORIZONTAL);
				rg.addView(rb);
			}
		singleAnswersLayout.addView(rg);
	}
	
}