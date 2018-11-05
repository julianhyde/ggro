function isMeasure(i) {
    return i >= 7 \
        || field_names[i] == "total_sightings" \
        || field_names[i] == "hours_counted";
}
function sanitize(s) {
    if (species_name[s]) return species_name[s];
    if (s == "total_sightings") return "total";
    if (s == "hours_counted") return "hours";
    gsub(/^.*_/, "", s);
    return s;
}
function sanitize2(s) {
    if (species_name[s]) return species_name[s];
    if (s == "total_sightings") return "total";
    if (s == "hours_counted") return "hours";
    gsub(/_/, " ", s);
    return s;
}
function julian(year, month, day_of_month) {
#    printf "julian(%d, %d, %d)...\n", year, month, day_of_month;
#    printf "\n";
    j = (year - 1) * 365 + (day_of_month - 1);
    for (m = 1; m < month; m++) {
#        printf "m=%d j=%d\n", m, j;
        j += month_days[m];
        if (m == 2 && year % 4 == 0) {
            ++j;
        }
    }
#    printf "julian(%d, %d, %d) = %d\n", year, month, day_of_month, j;
    return j;
}
function hoursToString(h) {
    s = int(h) + "";
    h -= int(h);
    h *= 60;
    h = int((h + 3) / 5) * 5;
    s = s ":";
    if (h < 10) {
        s = s "0";
    }
    s = s h;
    return s;
}
function intToString(d) {
    s = sprintf("%d", d);
    if (length(s) > 3) {
        xxl = substr(s, 1, length(s) - 3);
        xxr = substr(s, length(s) - 2, 3);
        s = xxl "," xxr;
    }
    return s;
}

FNR == 1 {
    day_offset = 18;
    for (i = 1; i <= NF; i++) {
        field_names[i] = $i;
    }
    field_count = NF;
    species_name["tuvu"] = "Turkey Vulture";
    species_name["ospr"] = "Osprey";
    species_name["wtki"] = "White-tailed Kite";
    species_name["baea"] = "Bald Eagle";
    species_name["noha"] = "Northern Harrier";
    species_name["ssha"] = "Sharp-shinned Hawk";
    species_name["coha"] = "Cooper's Hawk";
    species_name["gosh"] = "Northern Goshawk";
    species_name["rsha"] = "Red-shouldered Hawk";
    species_name["bwha"] = "Broad-winged Hawk";
    species_name["swha"] = "Swainson's Hawk";
    species_name["rtha"] = "Red-tailed Hawk";
    species_name["feha"] = "Ferruginous Hawk";
    species_name["rlha"] = "Rough-legged Hawk";
    species_name["goea"] = "Golden Eagle";
    species_name["amke"] = "American Kestrel";
    species_name["merl"] = "Merlin";
    species_name["pefa"] = "Peregrine Falcon";
    species_name["prfa"] = "Prairie Falcon";
    species_name["unid_accipiter"] = "Accipiter";
    species_name["unid_buteo"] = "Buteo";
    species_name["unid_eagle"] = "Eagle";
    species_name["unid_falcon"] = "Falcon";
    species_name["unid_raptor"] = "Raptor";
    month_days[1] = month_days[3] = month_days[5] = month_days[7] = \
    month_days[8] = month_days[10] = month_days[12] = 31;
    month_days[2] = 28;
    month_days[4] = month_days[6] = month_days[9] = month_days[11] = 30;
    week_days[0] = "Sun";
    week_days[1] = "Mon";
    week_days[2] = "Tue";
    week_days[3] = "Wed";
    week_days[4] = "Thu";
    week_days[5] = "Fri";
    week_days[6] = "Sat";
    year_months[8] = "August";
    year_months[9] = "September";
    year_months[10] = "October";
    year_months[11] = "November";
    year_months[12] = "December";
    day_count = 0;
}
FNR > 1 {
    date = $1;
    author = $2;
    total_sightings = $3;
    hours_counted = $4;
    hph = $5;
    total_species = $6;
    for (i = 1; i <= NF; i++) {
        totals[i] += $i;
        dow_totals[i, FNR % 7] = $i;
        week_totals[i] = 0;
        for (j = 0; j < 7; j++) {
            week_totals[i] += dow_totals[i, j];
        }
    }
    days[day_count] = date;
    day_totals[day_count] = total_sightings;
    day_hphs[day_count] = hph;
    day_hours[day_count] = hours_counted;
    day_species_counts[day_count] = total_species;

#    printf "\n%d\n", FNR;
#    for (i = 1; i < 20; i++) {
#printf "  %s: %d (", field_names[i], week_totals[i];
#for (j = 0; j < 7; j++) printf "%d ", dow_totals[i, j];
#printf ")\n";
#    }
    ++day_count;
}
END {
    total_hours = totals[4];

    printf "<html>\n";
    printf "<head>\n";
    printf "<title>GGRO reports: the season so far</title>\n";
    printf "<style>th { color: white; background-color: #000060 } </style>\n";
    printf "</head>\n";
    printf "<body>\n";
    printf "<h1>GGRO reports</h1>\n";
    printf "<p>These reports are updated daily based on the data in the <a href='http://www.parksconservancy.org/conservation/plants-animals/raptors/research/daily-hawk-count.html'>Daily Hawk Count</a> blog.</p>\n";
    printf "<p>Data have not been entirely checked &mdash; contact <a href='mailto:swilson@parksconservancy.org'>Step Wilson</a> for final results and for permission to use.</p>\n";

    if (total_hours + 0 == 0) {
        # Avoid divide-by-zero at start of season
        total_hours = 1;
    }

    # Horizontal table
    if (0) {
    printf "<table frame=border cellpadding=2 cellspacing=3>\n";

    printf "<tr>\n";
    printf "<td colspan=3>&nbsp;</td>\n";
    printf "<td colspan=5>Misc</td>\n";
    printf "<td colspan=3>Accipiter</td>\n";
    printf "<td colspan=7>Buteo</td>\n";
    printf "<td colspan=4>Falcon</td>\n";
    printf "<td colspan=5>Unidentified</td>\n";
    printf "</tr>\n";

    printf "<tr>\n";
    printf "<td>&nbsp;</td>\n";
    for (i = 1; i <= field_count; i++) {
        if (isMeasure(i)) {
            printf "<td>%s</td>\n", sanitize(field_names[i]);
        }
    }
    printf "</tr>\n";

    printf "<tr>\n";
    printf "<th>Count</th>\n";
    for (i = 1; i <= field_count; i++) {
        if (isMeasure(i)) {
            printf "<td>%s</td>\n", totals[i];
        }
    }
    printf "</tr>\n";

    printf "<tr>\n";
    printf "<th>Per hour</th>\n";
    for (i = 1; i <= field_count; i++) {
        if (isMeasure(i)) {
            printf "<td>%.1f</td>\n", totals[i]; # / total_hours;
        }
    }
    printf "</tr>\n";

    printf "</table>\n";
    }

    printf "<h2>Species totals for the season to date</h2>\n";

    printf "<table frame=border cellpadding=2 cellspacing=3>\n";
    printf "<tr><th>Species</th><th>Count</th><th>&nbsp;Per hour</th><th>Last week</th></tr>\n";
    for (i = 5; i <= field_count; i++) {
        if (field_names[i] == "unid_accipiter") {
            printf "<tr><td colspan=3>&nbsp;</td></tr>\n";
            printf "<tr><td>Unidentified:</td><td colspan=2>&nbsp;</td></tr>\n";
        }
        if (isMeasure(i)) {
            printf "<tr><td>%s</td><td align=right>%s</td><td align=right>%.1f</td><td align=right>%s</td>\n", \
                sanitize(field_names[i]), \
                intToString(totals[i]), \
                totals[i] / total_hours, \
                intToString(week_totals[i]);
        }
    }
    printf "<tr><td colspan=4>&nbsp;</td></tr>\n";
    printf "<tr><td>%s</td><td align=right>%s</td><td align=right>%.1f</td><td align=right>%s</td>\n", \
        "Total", \
        intToString(totals[3]), \
        totals[3] / total_hours, \
        intToString(week_totals[3]);
    printf "<tr><td>%s</td><td align=right>%s</td><td align=right>%.1f</td><td align=right>%.1f</td>\n", \
        "Hours", \
        totals[4], \
        totals[4] / total_hours, \
        week_totals[4];
    printf "</table>\n";

    printf "<h2>Daily totals</h2>\n";

    printf "<table frame=border cellpadding=2 cellspacing=3>\n";
    printf "<tr><th colspan=2>Date</th><th>Day</th><th>Hours</th><th>Species</th><th width=240>Count</th><th width=240>Per hour</th></tr>\n";
    max_day_total = 300;
    max_hph = 60;
    max_species_count = 1;
    for (i = 0; i < day_count; i++) {
        if (day_totals[i] > max_day_total) {
            max_day_total = day_totals[i];
            max_hph = day_hphs[i];
        }
        if (day_species_counts[i] > max_species_count) {
            max_species_count = day_species_counts[i];
        }
    }
    prev_month = "xxxx";
    for (i = 0; i < day_count; i++) {
        day = days[i];
        year = substr(day, 1, 4);
        month = substr(day, 5, 2) + 0;
        day_of_month = substr(day, 7, 2);
        printf "<tr>";
        if (prev_month == month) {
            printf "<td/>";
        } else {
            printf "<td>%s</td>", year_months[month];
            prev_month = month;
        }
        printf "<td><a href='http://www.parksconservancy.org/conservation/plants-animals/raptors/research/daily-hawk-count.html#%s' target=_new>%d</a></td>", day, day_of_month;
        year = substr(day, 1, 4) + 0;
        month = substr(day, 5, 2) + 0;
        day_of_month = substr(day, 7, 2) + 0;
        day_of_week = (julian(year, month, day_of_month) + 7) % 7;
        printf "<td align=right>%s&nbsp;%s</td>", \
            week_days[day_of_week], \
            ((julian(1, month, day_of_month - (day_of_week + 6) % 7) + day_offset) % 14) < 7 ? "I" : "II";
        printf "<td align=right>%s</td>", hoursToString(day_hours[i]);
        day_total = day_totals[i];
        hph = day_hphs[i];
        species_count = day_species_counts[i];
        if (day_total > 0) {
            printf "<td align=left><div style='background-color:#600000; color: white; width:%d'>%d</div></td>\n", \
                species_count / max_species_count * 100, \
                species_count;
            printf "<td align=right><div style='background-color:#000060; color: white; width:%d'>%d</div></td>\n", \
                day_total / max_day_total * 240, \
                day_total;
            printf "<td align=left><div style='background-color:olive; color: white; width:%d'>%.1f</div></td>\n", \
                hph / max_hph * 240, \
                hph;
        } else {
            printf "<td align=left>%d</td>", species_count;
            printf "<td align=right>%d</td>", day_total;
            printf "<td align=left>%.1f</td>", hph;
        }
        printf "</td>\n";
    }
    printf "</table>\n";
    printf "</body>\n";
    printf "</html>\n";
}

# end report.awk
