package nl.pdok.gml3.test;

import org.locationtech.jts.geom.Geometry;
import nl.pdok.gml3.GMLParser;
import nl.pdok.gml3.exceptions.GML3ParseException;
import nl.pdok.gml3.impl.GMLMultiVersionParserImpl;
import nl.pdok.gml3.impl.gml3_1_1_2.GML3112ParserImpl;
import nl.pdok.gml3.impl.gml3_2_1.GML321ParserImpl;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GML3ParserTest {

    public static final String GML2_MULTIPOLYGON = "<gml:MultiPolygon srsName=\"http://www.opengis.net/gml/srs/epsg.xml#28992\"><gml:polygonMember><gml:Polygon srsName=\"EPSG:28992\"><gml:outerBoundaryIs><gml:LinearRing><gml:coordinates xmlns:gml=\"http://www.opengis.net/gml\" decimal=\".\" cs=\",\" ts=\" \">118471.87235151,555231.52600588 118458.70601837,555266.86614134 118623.73395843,555330.54118789 118591.91478103,555416.09952221 118254.01019788,555286.96167575 118302.34755853,555166.03500393 118471.87235151,555231.52600588</gml:coordinates></gml:LinearRing></gml:outerBoundaryIs></gml:Polygon></gml:polygonMember></gml:MultiPolygon>";
    public static final String GML3_1_1_POLYGON = "<gml:Polygon xmlns:gml=\"http://www.opengis.net/gml\" srsName=\"urn:ogc:def:crs:EPSG::28992\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"2\" count=\"67\">116055.50175 488633.817954607 116055.900101069 488633.836930407 116056.294844608 488633.89368596 116056.682405761 488633.987707278 116057.059274711 488634.11814289 116057.422038477 488634.283811551 116057.767411813 488634.483212939 116058.092266965 488634.714541246 116058.393661994 488634.975701525 116058.66886742 488635.264328669 116058.915390941 488635.577808826 116059.131 488635.913303071 116059.313742009 488636.267773117 116059.461962029 488636.638008826 116059.574317755 488637.020657289 116059.649791677 488637.41225318 116059.687700291 488637.809250147 116059.687700291 488638.208052924 116059.649791677 488638.605049891 116059.574317755 488638.996645783 116059.461962029 488639.379294245 116059.313742009 488639.749529955 116059.131 488640.104 116058.915390941 488640.439494245 116058.66886742 488640.752974402 116058.393661994 488641.041601547 116058.092266965 488641.302761826 116057.767411813 488641.534090132 116057.422038477 488641.733491521 116057.059274711 488641.899160181 116056.682405761 488642.029595794 116056.294844608 488642.123617112 116055.900101069 488642.180372664 116055.50175 488642.199348465 116055.103398931 488642.180372664 116054.708655391 488642.123617112 116054.321094239 488642.029595794 116053.944225288 488641.899160181 116053.581461523 488641.733491521 116053.236088187 488641.534090132 116052.911233035 488641.302761826 116052.609838006 488641.041601547 116052.33463258 488640.752974402 116052.088109059 488640.439494245 116051.8725 488640.104 116051.689757991 488639.749529955 116051.541537971 488639.379294245 116051.429182245 488638.996645783 116051.353708323 488638.605049891 116051.315799709 488638.208052924 116051.315799709 488637.809250147 116051.353708323 488637.41225318 116051.429182245 488637.020657289 116051.541537971 488636.638008826 116051.689757991 488636.267773117 116051.8725 488635.913303071 116052.088109059 488635.577808826 116052.33463258 488635.264328669 116052.609838006 488634.975701525 116052.911233035 488634.714541246 116053.236088187 488634.483212939 116053.581461523 488634.283811551 116053.944225288 488634.11814289 116054.321094239 488633.987707278 116054.708655391 488633.89368596 116055.103398931 488633.836930407 116055.50175 488633.817954607</gml:posList></gml:LinearRing></gml:exterior></gml:Polygon>";
    public static final String GML3_2_1_SURFACE = "<?xml version=\"1.0\"?><gml:Surface xmlns:gml=\"http://www.opengis.net/gml/3.2\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" gml:id=\"ID1\" xsi:schemaLocation=\"http://www.opengis.net/gml/3.2 http://schemas.opengis.net/gml/3.2.1/gml.xsd\"><gml:patches><gml:PolygonPatch><gml:exterior><gml:LinearRing><gml:posList>114837.536 426864.413 114837.679 426860.151 114837.725 426858.791 114837.903 426853.491 114839.788 426797.446 114839.889 426774.424 114843.137 426714.397 114844.178 426626.525 114844.632 426569.202 114844.465 426545.996 114844.532 426520.755 114844.902 426431.545 114844.998 426419.57 114845.613 426409.136 114846.919 426398.705 114848.88 426389.032 114849.701 426383.846 114851.645 426371.31 114852.75 426362.321 114852.969 426359.068 114853.145 426356.453 114853.495 426351.264 114854.84 426338.326 114856.294 426328.382 114856.894 426322.465 114858.646 426304.201 114859.559 426290.013 114859.308 426281.757 114859.148 426243.653 114859.076 426226.602 114861.369 426226.36 114867.155 426225.75 114866.12 426119.135 114866.025 426109.306 114866 426106.766 114865.823 426088.488 114865.809 426087.003 114869.798 426086.833 114901.774 426085.574 114966.425 426082.845 115079.049 426069.211 115102.111 426059.072 115111.798 426054.813 115275.544 425984.85 115276.815 425985.454 115281.788 425987.436 115300.041 425994.367 115300.038 425819.205 115300.036 425717.366 115400.027 425725.919 115636.508 425742.262 115641.221 425742.588 115900.171 425757.119 115924.743 425757.635 115986.016 425759.022 116012.999 425758.644 116086.769 425755.494 116114.703 425754.416 116184.854 425751.465 116284.59 425747.686 116400 425742.9 116643 425764 116932 425770 117202.231 425769.014 117206 425769 117341.264 425762.877 117446.765 425758.65 117496.304 425756.575 117511.641 425755.605 117523.676 425754.339 117591.744 425747.386 117665.162 425740.139 117752.71 425731.212 117800.433 425726.868 117809.503 425726.436 117841.629 425723.097 117877.748 425719.461 117919.588 425715.109 117958.21 425711.115 117998.859 425706.882 118045.11 425702.232 118074.196 425699.251 118107.096 425695.793 118136.658 425692.336 118166.459 425689.057 118235.12 425681.307 118263.431 425678.147 118304.676 425673.557 118346.933 425668.787 118381.458 425664.606 118382.334 425664.182 118388.029 425663.516 118396.984 425662.334 118416.5 425660.068 118448.592 425656.456 118530.981 425646.624 118575.589 425641.547 118615.151 425636.711 118642.199 425633.459 118656.792 425631.85 118662.785 425631.359 118674.002 425630.506 118711.57 425627.719 118741.304 425625.565 118797.091 425621.362 118851.275 425617.298 118907.933 425613.349 118980.034 425607.002 119018.213 425603.518 119080.615 425597.823 119168.961 425589.67 119205.565 425586.286 119231.01 425584.044 119267.547 425580.952 119286.646 425579.257 119326.089 425575.684 119361.795 425572.416 119397.324 425569.444 119424.371 425567.082 119498.284 425566.964 119530.911 425567.031 119567.69 425566.913 119600.731 425566.86 119637.688 425566.921 119674.947 425566.852 119696.339 425568.716 119723.518 425571.212 119752.255 425573.895 119773.426 425576.307 119803.182 425579.712 119832.311 425583.603 119873.223 425589.533 119907.774 425594.754 119950.012 425602.165 119997.047 425611.125 120041.143 425620.334 120081.992 425629.12 120102.17 425633.801 120131.811 425641.132 120158.341 425647.997 120189.174 425656.986 120257.882 425677.146 120295.427 425576.227 120319.833 425510.625 120347.21 425436.655 120494.961 425556.644 120597.234 425639.379 120677.158 425704.034 120768.996 425753.845 121024.898 425891.837 121245.569 426014.552 121417.874 426109.231 121491.552 426149.224 121500.127 426153.88 121693.834 426259.333 121830.068 426320.873 121999.748 426397.238 122103.206 426443.508 122190.389 426482.367 122298.793 426517.265 122399.922 426550.498 122400.058 426550.543 122443.366 426564.302 122518.401 426588.476 122627.624 426623.721 122696.466 426646.014 122765.663 426661.766 122875.422 426686.302 122981.55 426709.86 123085.309 426718.321 123209.822 426728.76 123275.244 426733.97 123267.777 426796.526 123248.718 426949.701 123236.688 427050.907 123226.023 427140.626 123225.875 427141.871 123215.571 427228.555 123214.998 427233.376 123193.724 427412.353 123171.177 427599.605 123161.281 427682.743 123160.729 427687.378 123158.13 427709.218 123157.934 427710.866 123157.364 427715.657 123157.276 427716.393 123157.266 427716.482 123157.264 427716.497 123156.208 427727.836 123156.172 427728.224 123155.449 427735.985 123155.388 427736.64 123153.112 427761.075 123153.006 427762.213 123152.684 427765.667 123152.663 427765.943 123146.446 427847.076 123146.368 427848.094 123146.762 427875.051 123146.785 427876.645 123146.293 427883.069 123139.661 427969.646 123139.342 427973.808 123138.658 427982.73 123137.583 427996.764 123136.985 428004.574 123106.916 428004.897 123088.356 428004.884 122991.923 428004.817 122894.971 428004.61 122822.58 428001.642 122758.012 427999.479 122751.475 427994.111 122702.488 427991.164 122668.334 427989.529 122662.577 427993.172 122640.284 427991.216 122608.347 427988.414 122521.976 427980.614 122488.91 427977.147 122382.1 427964.645 122276.532 427949.844 122237.307 427944.022 122147.896 427928.718 122143.074 427922.757 122102.446 427914.457 122094.854 427913.009 122089.408 427911.971 122062.18 427906.779 122053.99 427909.833 122031.553 427905.217 122007.488 427900.315 121982.862 427894.785 121948.084 427887.068 121940.227 427885.334 121925.361 427882.054 121900.707 427876.188 121879.311 427870.893 121853.063 427864.148 121816.221 427855 121790.826 427848.353 121766.115 427841.816 121736.112 427833.613 121674.28 427816.601 121638.192 427806.376 121621.168 427800.911 121604.083 427795.483 121570.168 427785.038 121530.794 427771.675 121498.714 427760.809 121461.416 427748.244 121428.032 427737.253 121401.097 427727.6 121336.729 427704.293 121301.357 427690.359 121254.563 427672.094 121213.972 427656.46 121191.032 427647.551 121156.711 427632.641 121139.118 427625.324 121100.825 427609.433 121053.445 427589.796 121020.68 427576.113 120976.61 427558.11 120936.135 427541.291 120909.906 427530.511 120910.026 427534.231 120855.742 427511.775 120829.095 427500.205 120824.487 427490.73 120804.687 427482.903 120784.285 427474.73 120756.357 427471.353 120730.467 427468.65 120708.819 427463.719 120701.522 427461.958 120690.133 427458.387 120678.646 427455.145 120676.124 427453.791 120659.392 427444.806 120634.473 427431.005 120631.492 427429.379 120628.803 427427.305 120611.223 427411.334 120590.619 427403.116 120578.176 427398.153 120547.02 427385.804 120532.42 427380.144 120530.942 427379.571 120504.678 427369.967 120487.097 427363.665 120479.137 427360.812 120473.5 427358.792 120445.805 427349.096 120439.601 427347.073 120421.359 427341.123 120419.141 427340.417 120362.888 427322.508 120347.738 427317.966 120332.474 427313.82 120297.757 427304.264 120258.496 427293.726 120234.673 427287.17 120231.186 427286.869 120231.021 427286.854 120213.641 427285.283 120212.939 427285.124 120156.76 427272.383 120104.745 427260.587 120015.429 427242.565 119992.219 427238.6 119894.706 427224.683 119823.194 427215.463 119805.863 427213.465 119771.179 427210.451 119759.355 427209.42 119716.173 427205.583 119608.425 427196.306 119587.833 427194.516 119558.6 427192.581 119556.91 427192.476 119532.6 427190.961 119469.049 427187.002 119449.405 427185.778 119367.736 427181.016 119308.238 427177.548 119257.255 427174.575 119207.067 427171.649 119206.864 427221.925 119206.728 427255.698 119206.726 427256.122 119206.498 427312.616 119206.48 427313.005 119206.265 427317.767 119206.261 427317.78 119185.485 427392.739 119185.451 427392.86 119179.455 427401.389 119179.267 427401.631 119175.885 427405.981 119167.668 427416.549 119157.301 427429.883 119149.112 427440.412 119136.555 427456.567 119130.275 427464.643 119129.836 427465.208 119126.972 427468.216 119107.471 427488.697 119060.629 427446.717 119002.78 427391.713 118939.962 427329.403 118927.351 427316.661 118918.732 427309.214 118909.115 427301.605 118889.864 427287.936 118879.422 427282.518 118868.595 427279.457 118860.647 427278.239 118850.884 427278.758 118826.051 427280.354 118794.858 427282.698 118768.673 427284.231 118740.93 427286.954 118711.237 427289.21 118694.319 427289.771 118665.666 427286.579 118635.616 427282.725 118603.511 427279.355 118568.415 427274.358 118538.634 427267.854 118481.922 427255.319 118449.991 427248.347 118432.311 427243.344 118393.172 427229.554 118378.645 427224.478 118343.416 427209.82 118314.813 427199.42 118292.874 427195.034 118272.464 427193.863 118249.644 427193.823 118232.02 427195.179 118217.977 427199.516 118199.503 427207.822 118190.357 427213.303 118175.716 427224.493 118158.049 427238.963 118143.646 427251.807 118131.358 427262.917 118108.597 427283.05 118094.464 427293.344 118082.407 427300.002 118065.289 427307.568 118048.244 427311.83 118029.864 427313.88 118011.342 427314.577 117995.837 427313.247 117983.686 427311.845 117962.598 427307.514 117934.923 427300.477 117913.888 427295.648 117895.522 427288.337 117874.716 427279.454 117830.478 427251.052 117823.893 427273.405 117811.81 427291.033 117811.569 427291.384 117811.126 427299.476 117806.893 427315.849 117802.445 427338.317 117801.178 427344.717 117801.218 427345.583 117799.069 427355.719 117795.416 427363.42 117786.642 427399.028 117777.137 427431.128 117771.542 427450.025 117770.849 427456.59 117768.567 427463.146 117768.437 427464.094 117767.222 427472.983 117761.567 427495.381 117748.164 427548.002 117736.469 427594.42 117735.585 427597.84 117728.031 427627.049 117719.088 427660.783 117708.084 427704.795 117695.254 427757.062 117687.984 427786.297 117673.589 427842.598 117667.626 427865.684 117667.087 427867.77 117662.528 427885.419 117659.352 427897.714 117654.752 427915.246 117654.144 427917.562 117644.692 427953.586 117629.156 428013.166 117615.671 428066.135 117600.969 428123.22 117589.104 428169.843 117576.829 428218.204 117569.443 428247.411 117566.127 428259.323 117565.416 428261.875 117562.988 428268.137 117562.775 428268.686 117560.891 428274.006 117560.851 428274.121 117560.7 428275.083 117559.422 428283.224 117557.699 428291.991 117556.594 428296.994 117555.304 428302.326 117552.629 428312.849 117545.532 428342.171 117543.599 428350.775 117540.449 428366.181 117537.419 428382.218 117534.116 428400.873 117531.023 428419.082 117527.586 428439.652 117525.132 428453.374 117523.024 428465.95 117520.825 428478.994 117518.07 428493.628 117514.469 428513.802 117511.457 428528.507 117509.277 428540.219 117507.759 428547.327 117506.267 428555.532 117499.237 428591.301 117496.065 428606.824 117493.724 428619.354 117492.989 428623.095 117492.102 428630.69 117490.963 428639.571 117490.253 428643.802 117486.792 428663.882 117483.69 428679.662 117478.152 428707.226 117474.842 428723.451 117474.2 428727.331 117472.924 428736.913 117470.898 428753.39 117469.646 428763.463 117468.913 428767.811 117467.856 428773.188 117466.964 428779.022 117465.582 428784.76 117462.268 428799.77 117461.692 428802.085 117461.326 428804.563 117460.041 428811.716 117456.444 428833.104 117450.32 428867.351 117445.484 428895.659 117440.208 428925.739 117440.118 428926.254 117439.999 428926.988 117438.29 428937.555 117437.808 428940.254 117434.739 428957.458 117434.417 428959.128 117431.045 428979.566 117426.539 429003.207 117424.039 429015.651 117417.836 429048.842 117415.744 429059.618 117410.454 429087.883 117407.262 429105.251 117405.385 429117.301 117402.305 429133.226 117398.233 429156.634 117393.475 429183.307 117390.312 429200.56 117387.234 429217.467 117379.995 429256.655 117378.971 429263.272 117377.315 429270.96 117375.52 429280.699 117373.925 429290.12 117372.846 429297.46 117367.657 429324.886 117354.489 429395.98 117340.073 429476.579 117325.517 429558.739 117308.876 429650.321 117303.907 429675.547 117283.981 429784.64 117255.557 429937.514 117239.187 430030.101 117211.879 430180.829 117187.282 430313.068 117174.975 430378.922 117173.972 430384.284 117157.973 430373.671 117146.943 430365.346 117132.352 430354.884 117123.41 430348.388 117113.642 430341.87 117056.878 430305.093 117049.369 430300.348 117044.066 430296.635 116935.975 430220.343 116925.307 430212.952 116918.556 430208.47 116901.143 430197.327 116890.535 430190.472 116849.791 430164.25 116843.607 430160.344 116834.931 430154.756 116825.87 430149.06 116812.72 430141.445 116804.502 430136.452 116797.682 430131.848 116776.915 430117.475 116760.384 430104.889 116756.695 430101.777 116754.453 430100.07 116744.586 430093.92 116741.046 430091.827 116729.752 430083.37 116725.226 430080.208 116721.834 430077.583 116696.03 430059.983 116675.333 430045.282 116670.731 430042.038 116667.502 430039.689 116646.519 430026.165 116642.67 430023.474 116639.207 430021.037 116621.329 430007.085 116616.256 430002.967 116612.753 430000.353 116598.9 429990.604 116594.648 429987.706 116592.038 429985.7 116586.339 429981.398 116583.876 429979.286 116581.995 429978.037 116578.782 429976.069 116576.282 429974.479 116573.385 429972.524 116570.22 429970.146 116550.884 429954.678 116545.595 429950.424 116542.698 429948.469 116524.183 429937.325 116516.342 429932.746 116508.519 429928.119 116492.723 429919.826 116488.164 429917.455 116485.39 429915.6 116481.599 429913.038 116462.451 429899.192 116458.515 429896.308 116455.244 429894.211 116437.389 429883.585 116431.23 429880.072 116431.066 429879.973 116427.661 429877.915 116379.505 429846.591 116374.75 429843.61 116370.58 429840.637 116350.013 429825.451 116346.561 429823.125 116342.997 429821.238 116329.818 429814.366 116324.453 429811.582 116319.282 429808.71 116315.637 429806.471 116304.205 429799.085 116298.469 429795.303 116294.533 429792.419 116282.959 429783.855 116278.272 429779.99 116273.389 429776.64 116246.664 429757.94 116227.864 429744.869 116222.146 429741.041 116217.766 429737.774 116213.437 429735.222 116208.599 429732.317 116205.257 429730.407 116192.049 429721.919 116173.216 429710.066 116168.431 429707.02 116165.623 429704.831 116146.249 429690.311 116137.94 429684.002 116132.025 429679.565 116119.467 429671.055 116112.576 429666.087 116105.946 429661.699 116093.782 429654.41 116092.115 429654.151 116079.026 429645.066 116056.311 429629.761 116049.582 429625.496 116032.816 429614.962 116026.543 429610.763 116021.197 429607.505 116007.877 429597.903 116001.494 429593.288 115970.987 429571.014 115964.009 429565.852 115943.23 429549.676 115939.651 429546.98 115927.63 429538.889 115922.251 429535.296 115919.22 429533.13 115894.262 429514.566 115886.075 429508.784 115881.513 429505.716 115870.664 429499.193 115857.753 429491.192 115845.831 429483.405 115829.913 429473.033 115795.049 429449.694 115789.915 429446.301 115785.78 429443.235 115745.922 429416.557 115740.794 429413.006 115735.643 429409.233 115712.386 429391.689 115707.331 429387.524 115704.211 429385.591 115690.434 429377.476 115684.229 429373.944 115678.684 429370.931 115667.299 429365.114 115663.4 429363.261 115660.105 429361.368 115651.423 429356.042 115646.405 429352.908 115641.576 429349.417 115630.558 429340.796 115620.949 429332.977 115615.636 429328.928 115614.632 429328.051 115612.823 429326.47 115603.806 429318.501 115597.35 429312.949 115591.137 429308.451 115586.851 429305.22 115583.649 429303.364 115572.621 429297.735 115568.724 429295.454 115565.797 429293.435 115554.638 429285.189 115540.698 429275.245 115529.842 429267.328 115523.418 429262.964 115499.957 429246.52 115494.707 429242.87 115491.044 429240.677 115483.027 429236.138 115477.225 429232.813 115472.837 429230.132 115453.544 429217.516 115424.827 429199.131 115420.591 429196.615 115412.547 429191.583 115394.013 429180.914 115386.493 429176.617 115382.059 429173.918 115375.441 429169.642 115372.735 429168.027 115369.503 429166.533 115366.453 429166.393 115362.738 429167.017 115298.93 429193.483 115290.883 429188.664 115290.289 429188.308 115288.956 429187.51 115288.464 429187.216 115288.204 429187.06 115287.391 429186.578 115287.199 429186.464 115286.421 429185.989 115261.51 429170.801 115272.841 429136.922 115276.555 429125.823 115278.027 429121.423 115264.016 429113.173 115256.665 429108.845 115241.003 429098.931 115235.442 429096.185 115224.753 429090.906 115214.812 429085.107 115213.18 429084.174 115192.184 429071.81 115167.735 429057.414 115132.867 429034.664 115122.182 429028.55 115105.936 429020.326 115048.904 428985.082 115041.864 428981.842 115012.957 428963.762 115000.79 428954.217 114996.606 428950.733 114978.431 428938.918 114969.5 428932.639 114941.552 428916.066 114909.374 428896.984 114900.048 428890.497 114893.44 428883.162 114798.805 428816.761 114777.724 428805.139 114770.613 428802.948 114770.901 428796.466 114771.044 428793.24 114771.192 428789.622 114771.244 428788.335 114771.859 428773.214 114773.643 428718.96 114774.279 428699.629 114774.408 428695.702 114775.87 428662.777 114776.038 428659.004 114777.722 428595.825 114779.505 428554.147 114779.506 428538.847 114779.101 428526.699 114779.407 428521.922 114780.069 428505.657 114780.68 428492.645 114781.087 428487.46 114781.345 428484.425 114781.378 428484.033 114781.596 428481.463 114781.851 428472.568 114783.682 428461.183 114784.089 428456.608 114784.852 428446.951 114785.056 428439.175 114785.921 428428.298 114785.059 428416.048 114784.652 428413.049 114785.264 428399.935 114786.332 428389.77 114787.349 428378.385 114788.52 428358.918 114788.521 428349.21 114788.777 428339.044 114790.613 428272.917 114791.841 428195.811 114793.316 428176.243 114794.285 428146.407 114799.231 427990.366 114799.947 427945.434 114801.218 427940.046 114802.438 427933.591 114804.421 427927.289 114807.268 427919.665 114809.759 427914.329 114814.283 427906.147 114821.348 427895.881 114842.746 427865.388 114844.83 427862.949 114846.761 427862.441 114850.37 427860.612 114852.403 427860.612 114853.622 427861.528 114853.612 427860.492 114853.444 427843.538 114853.417 427840.853 114853.388 427837.928 114853.371 427836.164 114853.371 427834.741 114852.812 427833.572 114852.202 427832.657 114850.576 427831.589 114848.848 427831.081 114846.053 427829.911 114841.48 427826.86 114836.601 427823.505 114833.095 427820.099 114828.776 427816.387 114827.432 427814.887 114823.491 427810.49 114818.664 427804.898 114816.885 427800.679 114814.243 427796.205 114810.229 427785.48 114807.435 427774.195 114806.826 427768.757 114805.302 427763.064 114805.304 427742.072 114807.902 427688.855 114810.296 427631.318 114812.079 427595.892 114813.964 427549.181 114816.358 427497.489 114816.817 427482.139 114818.552 427408.134 114820.54 427355.781 114820.795 427343.532 114820.796 427335.586 114820.796 427333.875 114821.282 427302.212 114822.164 427280.81 114824.676 427219.84 114826.736 427162.13 114826.954 427156.026 114826.976 427155.408 114827.217 427148.659 114827.934 427128.589 114831.366 426998.568 114833.537 426947.107 114834.104 426941.784 114834.601 426939.4 114835.583 426918.561 114836.139 426903.159 114836.336 426897.705 114836.847 426883.529 114837.078 426877.14 114837.096 426876.648 114837.193 426873.94 114837.529 426864.607 114837.536 426864.413</gml:posList></gml:LinearRing></gml:exterior></gml:PolygonPatch></gml:patches></gml:Surface>";
    public static final String GML3_2_1_MULTICURVE = "<gml:MultiCurve xmlns:gml=\"http://www.opengis.net/gml/3.2\" gml:id=\"GEOMETRY_955ec3f8-18e0-43c9-8904-9b5d2617d708\" srsName=\"urn:ocg:def:crs:EPSG::28992\"><gml:curveMember><gml:LineString gml:id=\"GEOMETRY_b93ff03b-c9a0-4ba8-a11d-dd02fead53c8\" srsName=\"urn:ocg:def:crs:EPSG::28992\"><gml:posList>264901.354000 564810.248000 264925.225000 564798.040000</gml:posList></gml:LineString></gml:curveMember></gml:MultiCurve>";

    public static String BAG_PAND_POLYGON = "<gml:Polygon xmlns:gml=\"http://www.opengis.net/gml\" srsName=\"urn:ogc:def:crs:EPSG::28992\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"3\" count=\"5\">253855.133 593180.537 0.0 253854.579 593177.588 0.0 253856.544 593177.219 0.0 253857.098 593180.167 0.0 253855.133 593180.537 0.0</gml:posList></gml:LinearRing></gml:exterior></gml:Polygon>";

    public static String GML3_1_1_POINT_3D = "<gml:Point xmlns:gml=\"http://www.opengis.net/gml\" srsName=\"urn:ogc:def:crs:EPSG::28992\"><gml:pos>255842.0 572888.0 0.0</gml:pos></gml:Point>";

    public static String GML3_1_1_POINT_2D = "<gml:Point xmlns:gml=\"http://www.opengis.net/gml\" srsName=\"urn:ogc:def:crs:EPSG::28992\"><gml:pos>255842.0 572888.0</gml:pos></gml:Point>";

    public static String GML3_2_1_POINT_3D = "<gml:Point xmlns:gml=\"http://www.opengis.net/gml/3.2\" gml:id=\"GEOMETRY_bf8088bb-e9ac-4a42-8536-fd1f2829405c\" srsName=\"urn:ogc:def:crs:EPSG::28992\"><gml:pos>255842.0 572888.0 0.0</gml:pos></gml:Point>";

    public static String GML3_2_1_POINT_2D = "<gml:Point xmlns:gml=\"http://www.opengis.net/gml/3.2\" gml:id=\"GEOMETRY_bf8088bb-e9ac-4a42-8536-fd1f2829405c\" srsName=\"urn:ogc:def:crs:EPSG::28992\"><gml:pos>255842.0 572888.0</gml:pos></gml:Point>";

    public static String GML3_2_1_POLYGON_3D = "<gml:Polygon xmlns:gml=\"http://www.opengis.net/gml/3.2\" gml:id=\"GEOMETRY_6429d89b-5b8c-4000-91b0-0868e2cc8ed6\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"3\" srsName=\"urn:ogc:def:crs:EPSG::28992\">253855.133 593180.537 1.234 253854.579 593177.588 0.0 253856.544 593177.219 0.0 253857.098 593180.167 0.0 253856.096 593180.317 0.0 253855.133 593180.537 1.234</gml:posList></gml:LinearRing></gml:exterior></gml:Polygon>";

    @Test
    public void testArcs() throws IOException, GML3ParseException {
        assertGml3_1_1_AndWkt("arcs");
    }

    @Test
    public void testMultipolygons() throws IOException, GML3ParseException {
        assertGml3_1_1_AndWkt("polygon-patches");
    }

    @Test
    public void top10LinearRing() throws GML3ParseException {
        GMLParser parser = new GML3112ParserImpl(0.001, 28992);
        Geometry geometry = parser.toJTSGeometry(GML3_1_1_POLYGON);
        assertNotNull(geometry);
    }

    @Test(expected = GML3ParseException.class)
    public void testGML_3_1_1_2_Parser_with_3_2_1_geom() throws GML3ParseException {
        GMLParser parser = new GML3112ParserImpl();
        Geometry geometry = parser.toJTSGeometry(GML3_2_1_SURFACE);
        assertNotNull(geometry);
    }

    @Test
    public void testGML3_2_1_Multisurface() throws GML3ParseException {
        GMLParser parser = new GML321ParserImpl();
        Geometry geometry = parser.toJTSGeometry(GML3_2_1_SURFACE);
        assertNotNull(geometry);
    }

    @Test(expected = GML3ParseException.class)
    public void testGML_3_2_1_Parser_with_3_1_1_geom() throws GML3ParseException {
        GMLParser parser = new GML321ParserImpl();
        Geometry geometry = parser.toJTSGeometry(GML3_1_1_POLYGON);
        assertNotNull(geometry);
    }

//    @Test
//    public void testGML3_2_1_Multicurve() throws GML3ParseException {
//        GMLParser parser = new GML321ParserImpl();
//        Geometry geometry = parser.toJTSGeometry(GML3_2_1_MULTICURVE);
//        assertNotNull(geometry);
//    }

    @Test(expected = GML3ParseException.class)
    public void invalidInputString() throws GML3ParseException {
        String gml = "<gml:Polygon xmlns:gml=\"http://www.opengis.net/gml\" srsName=\"urn:ogc:def:crs:EPSG::28992\"><gml:exterior><gml:LinearRing><gml:posList srsDimension=\"2\" count=\"67\">116055.50175 488633.817954607 116055.900101069 488633.836930407 116056.294844608 488633.89368596 116056.682405761 488633.987707278 116057.059274711 488634.11814289 116057.422038477 488634.283811551 116057.767411813 488634.483212939 116058.092266965 488634.714541246 116058.393661994 488634.975701525 116058.66886742 488635.264328669 116058.915390941 488635.577808826 116059.131 488635.913303071 116059.313742009 488636.267773117 116059.461962029 488636.638008826 116059.574317755 488637.020657289 116059.649791677 488637.41225318 116059.687700291 488637.809250147 116059.687700291 488638.208052924 116059.649791677 488638.605049891 116059.574317755 488638.996645783 116059.461962029 488639.379294245 116059.313742009 488639.749529955 116059.131 488640.104 116058.915390941 488640.439494245 116058.66886742 488640.752974402 116058.393661994 488641.041601547 116058.092266965 488641.302761826 116057.767411813 488641.534090132 116057.422038477 488641.733491521 116057.059274711 488641.899160181 116056.682405761 488642.029595794 116056.294844608 488642.123617112 116055.900101069 488642.180372664 116055.50175 488642.199348465 116055.103398931 488642.180372664 116054.708655391 488642.123617112 116054.321094239 488642.029595794 116053.944225288 488641.899160181 116053.581461523 488641.733491521 116053.236088187 488641.534090132 116052.911233035 488641.302761826 116052.609838006 488641.041601547 116052.33463258 488640.752974402 116052.088109059 488640.439494245 116051.8725 488640.104 116051.689757991 488639.749529955 116051.541537971 488639.379294245 116051.429182245 488638.996645783 116051.353708323 488638.605049891 116051.315799709 488638.208052924 116051.315799709 488637.809250147 116051.353708323 488637.41225318 116051.42";
        GMLParser parser = new GML3112ParserImpl(0.001, 28992);
        Geometry geometry = parser.toJTSGeometry(gml);
        assertNotNull(geometry);
    }

    @Test
    public void testGML3xxPoints() throws GML3ParseException {
        GMLParser parser = new GMLMultiVersionParserImpl(0.001, 28992);

        assertNotNull(parser.toJTSGeometry(GML3_1_1_POINT_3D));
        assertNotNull(parser.toJTSGeometry(GML3_1_1_POINT_2D));
        assertNotNull(parser.toJTSGeometry(GML3_2_1_POINT_3D));
        assertNotNull(parser.toJTSGeometry(GML3_2_1_POINT_2D));
    }

    @Test
    public void testGMl3xxParser() throws GML3ParseException {
        GMLParser parser = new GMLMultiVersionParserImpl(0.001, 28992);

        assertNotNull(parser.toJTSGeometry(GML3_1_1_POLYGON));
        assertNotNull(parser.toJTSGeometry(GML3_1_1_POLYGON));
        assertNotNull(parser.toJTSGeometry(GML3_2_1_SURFACE));
        assertNotNull(parser.toJTSGeometry(GML3_1_1_POLYGON));
        assertNotNull(parser.toJTSGeometry(GML3_2_1_MULTICURVE));
        assertNotNull(parser.toJTSGeometry(BAG_PAND_POLYGON));
        assertNotNull(parser.toJTSGeometry(GML3_2_1_POLYGON_3D));
    }

    @Test(expected = GML3ParseException.class)
    public void testGMl3xxParserWithGml2Polygon() throws GML3ParseException {
        GMLParser parser = new GMLMultiVersionParserImpl(0.001, 28992);
        parser.toJTSGeometry(GML2_MULTIPOLYGON);
    }

    @Test
    public void testLandsgrensBestuurlijkeGrenzen() throws IOException, GML3ParseException {
        String gml = FileUtils.readFileToString(new File(GML3ParserTest.class.getResource("/landsgrens.gml").getFile()), "UTF-8");
        GMLParser parser = new GML321ParserImpl(0.001, 28992);
        Geometry geometry = parser.toJTSGeometry(gml);
        assertNotNull(geometry);
    }

    private void assertGml3_1_1_AndWkt(String testGeometry) throws IOException, GML3ParseException {
        String expectedWkt = FileUtils.readFileToString(new File(GML3ParserTest.class.getResource("/" + testGeometry + ".wkt").getFile()), "UTF-8");
        InputStream withGMLArcs = GML3ParserTest.class.getResourceAsStream("/" + testGeometry + ".gml");
        String wkt = gml_3_1_1_ToWkt(withGMLArcs);
        assertEquals(expectedWkt, wkt);
    }

    private String gml_3_1_1_ToWkt(InputStream withGMLArcs) throws IOException, GML3ParseException {
        GMLParser gml3Parser = new GML3112ParserImpl(0.001, 28992);
        Geometry geo = gml3Parser.toJTSGeometry(new InputStreamReader(withGMLArcs));
        return geo.toText();
    }
}
