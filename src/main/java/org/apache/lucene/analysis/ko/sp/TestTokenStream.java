package org.apache.lucene.analysis.ko.sp;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class TestTokenStream
{
	public static void main( String [] args )
	{
		
		
		int iType = 3;
		
		String strInput = "아버지가방에들어가신다";

		try
		{
			Analyzer clsAnalyzer = null;
			
			switch( iType )
			{
			case 1:
                // 기본 KoreanAnalyzer 객체를 생성한다.
				clsAnalyzer = new KoreanAnalyzer( );
				break;
			case 2:
                // "한식당" 단어가 포함된 사용자 사전을 이용하여서 KoreanAnalyzer 객체를 생성한다.
				//UserDictionary clsDict = UserDictionary.open( new StringReader( "한식당" ) );
				//clsAnalyzer = new KoreanAnalyzer( clsDict, DecompoundMode.NONE, KoreanPartOfSpeechStopFilter.DEFAULT_STOP_TAGS, false );
				break;
			default:
				clsAnalyzer = new StandardAnalyzer();
				break;
			}
	
			TokenStream clsStream = clsAnalyzer.tokenStream( "string", strInput );
			CharTermAttribute clsAttr = clsStream.addAttribute( CharTermAttribute.class );
		
			clsStream.reset();
			
			while( clsStream.incrementToken( ) )
			{
				System.out.println( "[" + clsAttr + "]" );
			}
			
			clsStream.close( );
			clsAnalyzer.close( );
		}
		catch( Exception e )
		{
			System.out.println( "ERROR(" + e.toString( ) + ")" );
		}
	}
}


